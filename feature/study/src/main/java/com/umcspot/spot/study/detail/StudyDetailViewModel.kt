package com.umcspot.spot.study.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.study.detail.model.StudyDetailSideEffect
import com.umcspot.spot.study.detail.model.StudyDetailState
import com.umcspot.spot.study.model.MemoirCreateModel
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.token.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class StudyDetailViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyDetailState())
    val uiState: StateFlow<StudyDetailState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<StudyDetailSideEffect>()
    val sideEffect: SharedFlow<StudyDetailSideEffect> = _sideEffect.asSharedFlow()

    private var currentUserId: String = ""

    init {
        loadMyUserId()
    }

    private fun loadMyUserId() {
        viewModelScope.launch {
            runCatching { tokenRepository.getUserId() }
                .onSuccess { id ->
                    currentUserId = id
                    _uiState.update { it.copy(myUserId = id) }
                }
        }
    }

    // ── 스터디 홈 데이터 조회 ──────────────────────────────────
    fun fetchStudyHomeDetail(studyId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val detailDeferred = async { studyRepository.getStudyDetail(studyId) }
            val membersDeferred = async { studyRepository.getStudyMembers(studyId) }
            val schedulesDeferred = async { studyRepository.getUpcomingSchedules(studyId) }
            val memoirsDeferred = async { studyRepository.getStudyRecentMemoirs(studyId) }

            detailDeferred.await().onSuccess { model ->
                _uiState.update { state ->
                    state.copy(homeState = state.homeState.copy(
                        studyTitle = model.title,
                        studyDescription = model.description,
                        thumbnailUrl = model.thumbnailUrl,
                        categories = model.categories.toPersistentList(),
                        currentMembers = model.currentMembers,
                        totalMembers = model.totalMembers,
                        likeCount = model.likeCount,
                        hitCount = model.hitCount
                    ))
                }
            }.onFailure { emitError(it) }

            membersDeferred.await().onSuccess { members ->
                _uiState.update { it.copy(homeState = it.homeState.copy(members = members.toPersistentList())) }
            }.onFailure { emitError(it) }

            schedulesDeferred.await().onSuccess { schedules ->
                val now = LocalDateTime.now()
                val processed = schedules.map { it.copy(isNow = !now.isBefore(it.startAt) && !now.isAfter(it.endAt)) }.toPersistentList()
                _uiState.update { it.copy(homeState = it.homeState.copy(schedules = processed)) }
            }.onFailure { emitError(it) }

            memoirsDeferred.await().onSuccess { memoirs ->
                _uiState.update { it.copy(homeState = it.homeState.copy(recentMemoirs = memoirs.toPersistentList())) }
            }.onFailure { emitError(it) }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    // ── 일정 관리 (Planner) ──────────────────────────────────
    fun fetchMonthlySchedules(studyId: Long, year: Int, month: Int) {
        viewModelScope.launch {
            studyRepository.getMonthlySchedules(studyId, year, month)
                .onSuccess { schedules ->
                    _uiState.update { state ->
                        state.copy(plannerState = state.plannerState.copy(monthlySchedules = schedules.toPersistentList()))
                    }
                    updateFilteredSchedules()
                }
        }
    }

    fun createSchedule(studyId: Long, title: String, location: String, startAt: LocalDateTime, endAt: LocalDateTime) {
        val tempId = System.currentTimeMillis() * -1
        val tempSchedule = StudyScheduleModel(tempId, title, startAt, endAt, false, true)

        _uiState.update { state ->
            val updatedMonthly = (state.plannerState.monthlySchedules + tempSchedule).sortedBy { it.startAt }.toPersistentList()
            state.copy(
                plannerState = state.plannerState.copy(
                    monthlySchedules = updatedMonthly,
                    selectedDaySchedules = computeFilteredSchedules(updatedMonthly, state.plannerState.selectedDate),
                    isScheduleCreateSuccess = true
                )
            )
        }

        viewModelScope.launch {
            _sideEffect.emit(StudyDetailSideEffect.ScheduleCreateSuccess)
            studyRepository.createSchedule(studyId, title, location, startAt, endAt)
                .onSuccess {
                    val date = _uiState.value.plannerState.selectedDate
                    fetchMonthlySchedules(studyId, date.year, date.monthValue)
                    fetchStudyHomeDetail(studyId)
                }
                .onFailure {
                    fetchMonthlySchedules(studyId, startAt.year, startAt.monthValue)
                    emitError(it)
                }
        }
    }

    private fun computeFilteredSchedules(schedules: List<StudyScheduleModel>, selectedDate: LocalDate) =
        schedules.filter {
            val start = it.startAt.toLocalDate()
            val end = it.endAt.toLocalDate()
            !selectedDate.isBefore(start) && !selectedDate.isAfter(end)
        }.sortedBy { it.startAt }.toPersistentList()

    private fun updateFilteredSchedules() {
        _uiState.update { state ->
            state.copy(plannerState = state.plannerState.copy(
                selectedDaySchedules = computeFilteredSchedules(state.plannerState.monthlySchedules, state.plannerState.selectedDate)
            ))
        }
    }

    fun deleteSchedule(studyId: Long, scheduleId: Long) {
        _uiState.update { state ->
            state.copy(
                plannerState = state.plannerState.copy(
                    monthlySchedules = state.plannerState.monthlySchedules.filterNot { it.id == scheduleId }.toPersistentList(),
                    expandedScheduleId = -1L
                )
            )
        }
        updateFilteredSchedules()
        viewModelScope.launch {
            studyRepository.deleteSchedule(studyId, scheduleId)
                .onSuccess { fetchStudyHomeDetail(studyId) }
                .onFailure { emitError(it) }
        }
    }

    fun toggleScheduleMenu(scheduleId: Long) {
        _uiState.update { state ->
            val nextId = if (state.plannerState.expandedScheduleId == scheduleId) -1L else scheduleId
            state.copy(plannerState = state.plannerState.copy(expandedScheduleId = nextId))
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(plannerState = state.plannerState.copy(selectedDate = date))
        }
        updateFilteredSchedules()
    }

    // ── 할 일 (Todo) ──────────────────────────────────
    fun createTodo(studyId: Long, content: String) {
        val selectedDate = uiState.value.plannerState.selectedDate
        viewModelScope.launch {
            studyRepository.createTodo(studyId, content, selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .onSuccess { newId ->
                    val newTodo = TodoModel(newId, currentUserId, content, false)
                    _uiState.update { it.copy(plannerState = it.plannerState.copy(todoList = (it.plannerState.todoList + newTodo).toPersistentList())) }
                }
                .onFailure { emitError(it) }
        }
    }

    fun toggleTodoStatus(studyId: Long, todoId: Long, isCurrentlyCompleted: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlyCompleted) studyRepository.uncompleteTodo(studyId, todoId) else studyRepository.completeTodo(studyId, todoId)
            result.onSuccess {
                _uiState.update { state ->
                    val newList = state.plannerState.todoList.map { if (it.id == todoId) it.copy(isCompleted = !isCurrentlyCompleted) else it }.toPersistentList()
                    state.copy(plannerState = state.plannerState.copy(todoList = newList))
                }
            }
        }
    }

    fun deleteTodo(studyId: Long, todoId: Long) {
        viewModelScope.launch {
            studyRepository.deleteTodo(studyId, todoId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(plannerState = state.plannerState.copy(todoList = state.plannerState.todoList.filterNot { it.id == todoId }.toPersistentList()))
                    }
                }
                .onFailure { emitError(it) }
        }
    }

    fun fetchMemberTodos(studyId: Long, memberId: Long, date: LocalDate) {
        viewModelScope.launch {
            studyRepository.getMemberTodos(studyId, memberId, date.toString())
                .onSuccess { todoList ->
                    _uiState.update { state ->
                        state.copy(plannerState = state.plannerState.copy(selectedMemberId = memberId.toString(), todoList = todoList.toPersistentList()))
                    }
                }
        }
    }

    // ── 회고록 (Memoir) ──────────────────────────────────
    fun postMemoir(studyId: Long, activity: String, learned: String, encouragement: String, isPrivate: Boolean, imageFiles: List<File>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            studyRepository.postMemoir(studyId, MemoirCreateModel(activity, learned, encouragement, isPrivate), imageFiles)
                .onSuccess {
                    _sideEffect.emit(StudyDetailSideEffect.MemoirPostSuccess)
                    fetchAllMemoirs(studyId)
                    fetchStudyHomeDetail(studyId)
                }
                .onFailure { emitError(it) }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun fetchAllMemoirs(studyId: Long, cursor: Long? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (currentUserId.isEmpty()) currentUserId = tokenRepository.getUserId()

            studyRepository.getFullStudyMemoirs(studyId, cursor, 20)
                .onSuccess { memoirs ->
                    val processed = memoirs.map { it.copy(isMyMemoir = it.memberId.toString() == currentUserId) }
                    _uiState.update { state ->
                        val currentList = if (cursor == null) emptyList() else state.memoirState.memoirs
                        state.copy(
                            isLoading = false,
                            memoirState = state.memoirState.copy(memoirs = (currentList + processed).toPersistentList())
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    emitError(it)
                }
        }
    }

    fun toggleMemoirReaction(studyId: Long, memoirId: Long, reactionType: String) {
        val memoir = _uiState.value.memoirState.memoirs.find { it.memoirId == memoirId } ?: return
        val isCurrentlySelected = when (reactionType) {
            "FIRE" -> memoir.reactions.isFired
            "HEART" -> memoir.reactions.isHearted
            "STAR" -> memoir.reactions.isStarred
            "SMILE" -> memoir.reactions.isSmiled
            else -> return
        }

        updateMemoirReactionUIState(memoirId, reactionType, !isCurrentlySelected)

        viewModelScope.launch {
            val result = if (isCurrentlySelected) {
                studyRepository.deleteReviewReaction(studyId, memoirId, reactionType)
            } else {
                studyRepository.postReviewReaction(studyId, memoirId, reactionType)
            }
            result.onFailure {
                updateMemoirReactionUIState(memoirId, reactionType, isCurrentlySelected)
                emitError(it)
            }
        }
    }

    private fun updateMemoirReactionUIState(memoirId: Long, reactionType: String, isSelected: Boolean) {
        _uiState.update { state ->
            val updatedMemoirs = state.memoirState.memoirs.map { memoir ->
                if (memoir.memoirId != memoirId) return@map memoir

                val diff = if (isSelected) 1 else -1
                memoir.copy(
                    reactions = when (reactionType) {
                        "FIRE" -> memoir.reactions.copy(isFired = isSelected)
                        "HEART" -> memoir.reactions.copy(isHearted = isSelected)
                        "STAR" -> memoir.reactions.copy(isStarred = isSelected)
                        "SMILE" -> memoir.reactions.copy(isSmiled = isSelected)
                        else -> memoir.reactions
                    },
                    reactionCounts = when (reactionType) {
                        "FIRE" -> memoir.reactionCounts.copy(fireCount = (memoir.reactionCounts.fireCount + diff).coerceAtLeast(0))
                        "HEART" -> memoir.reactionCounts.copy(heartCount = (memoir.reactionCounts.heartCount + diff).coerceAtLeast(0))
                        "STAR" -> memoir.reactionCounts.copy(starCount = (memoir.reactionCounts.starCount + diff).coerceAtLeast(0))
                        "SMILE" -> memoir.reactionCounts.copy(smileCount = (memoir.reactionCounts.smileCount + diff).coerceAtLeast(0))
                        else -> memoir.reactionCounts
                    }
                )
            }.toPersistentList()
            state.copy(memoirState = state.memoirState.copy(memoirs = updatedMemoirs))
        }
    }

    fun deleteMemoir(studyId: Long, memoirId: Long) {
        viewModelScope.launch {
            studyRepository.deleteMemoir(studyId, memoirId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(memoirState = state.memoirState.copy(
                            memoirs = state.memoirState.memoirs.filterNot { it.memoirId == memoirId }.toPersistentList()
                        ))
                    }
                }
                .onFailure { emitError(it) }
        }
    }

    private suspend fun emitError(t: Throwable) {
        _sideEffect.emit(StudyDetailSideEffect.ShowSnackBar(t.message ?: "오류가 발생했습니다."))
    }
}