package com.umcspot.spot.study.detail

import android.util.Log
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

    fun fetchStudyHomeDetail(studyId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val detailDeferred = async { studyRepository.getStudyDetail(studyId) }
            val membersDeferred = async { studyRepository.getStudyMembers(studyId) }
            val schedulesDeferred = async { studyRepository.getUpcomingSchedules(studyId) }
            val memoirsDeferred = async { studyRepository.getStudyRecentMemoirs(studyId) }

            detailDeferred.await().onSuccess { model ->
                _uiState.update { it.copy(homeState = it.homeState.copy(
                    studyTitle = model.title,
                    studyDescription = model.description,
                    thumbnailUrl = model.thumbnailUrl,
                    categories = model.categories.toPersistentList(),
                    currentMembers = model.currentMembers,
                    totalMembers = model.totalMembers,
                    likeCount = model.likeCount,
                    hitCount = model.hitCount
                ))}
            }.onFailure { emitError(it) }

            membersDeferred.await().onSuccess { members ->
                _uiState.update { it.copy(homeState = it.homeState.copy(members = members.toPersistentList())) }
            }.onFailure { emitError(it) }

            schedulesDeferred.await().onSuccess { schedules ->
                val now = LocalDateTime.now()
                val processedSchedules = schedules.map { schedule ->
                    val isCurrent = !now.isBefore(schedule.startAt) && !now.isAfter(schedule.endAt)
                    schedule.copy(isNow = isCurrent)
                }.toPersistentList()

                _uiState.update { it.copy(homeState = it.homeState.copy(schedules = processedSchedules)) }
            }.onFailure { emitError(it) }

            memoirsDeferred.await().onSuccess { memoirs ->
                _uiState.update { it.copy(homeState = it.homeState.copy(recentMemoirs = memoirs.toPersistentList())) }
            }.onFailure { emitError(it) }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun fetchMonthlySchedules(studyId: Long, year: Int, month: Int) {
        viewModelScope.launch {
            studyRepository.getMonthlySchedules(studyId, year, month).onSuccess { schedules ->
                _uiState.update { state ->
                    state.copy(plannerState = state.plannerState.copy(
                        monthlySchedules = schedules.toPersistentList()
                    ))
                }
                updateFilteredSchedules()
            }.onFailure {
                Log.e("PlannerDebug", it.message.toString())
            }
        }
    }

    fun createSchedule(
        studyId: Long,
        title: String,
        location: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ) {
        val tempId = System.currentTimeMillis() * -1
        val tempSchedule = StudyScheduleModel(
            id = tempId,
            title = title,
            startAt = startAt,
            endAt = endAt,
            isNow = false,
            isMine = true
        )

        _uiState.update { state ->
            val updatedMonthly = (state.plannerState.monthlySchedules + tempSchedule)
                .sortedBy { it.startAt }
                .toPersistentList()

            state.copy(
                plannerState = state.plannerState.copy(
                    monthlySchedules = updatedMonthly,
                    isScheduleCreateSuccess = true
                )
            )
        }
        updateFilteredSchedules()

        viewModelScope.launch {
            studyRepository.createSchedule(studyId, title, location, startAt, endAt)
                .onSuccess {
                    val date = _uiState.value.plannerState.selectedDate
                    fetchMonthlySchedules(studyId, date.year, date.monthValue)
                    fetchStudyHomeDetail(studyId)
                    _sideEffect.emit(StudyDetailSideEffect.ScheduleCreateSuccess)
                }
                .onFailure {
                    _uiState.update { state ->
                        val rolledBack = state.plannerState.monthlySchedules.filterNot { it.id == tempId }.toPersistentList()
                        state.copy(plannerState = state.plannerState.copy(monthlySchedules = rolledBack))
                    }
                    updateFilteredSchedules()
                    emitError(it)
                }
        }
    }

    private fun updateFilteredSchedules() {
        _uiState.update { state ->
            val date = state.plannerState.selectedDate
            val now = LocalDateTime.now()

            val filtered = state.plannerState.monthlySchedules
                .filter { schedule ->
                    val start = schedule.startAt.toLocalDate()
                    val end = schedule.endAt.toLocalDate()
                    !date.isBefore(start) && !date.isAfter(end)
                }
                .map { schedule ->
                    val isCurrent = !now.isBefore(schedule.startAt) && !now.isAfter(schedule.endAt)
                    schedule.copy(isNow = isCurrent)
                }
                .sortedBy { it.startAt }
                .toPersistentList()

            state.copy(plannerState = state.plannerState.copy(selectedDaySchedules = filtered))
        }
    }

    fun deleteSchedule(studyId: Long, scheduleId: Long) {
        _uiState.update { state ->
            val updatedMonthly = state.plannerState.monthlySchedules
                .filterNot { it.id == scheduleId }
                .toPersistentList()

            state.copy(
                plannerState = state.plannerState.copy(
                    monthlySchedules = updatedMonthly,
                    expandedScheduleId = -1L
                )
            )
        }
        updateFilteredSchedules()

        viewModelScope.launch {
            studyRepository.deleteSchedule(studyId, scheduleId)
                .onSuccess {
                    val date = _uiState.value.plannerState.selectedDate
                    fetchMonthlySchedules(studyId, date.year, date.monthValue)
                }
                .onFailure {
                    Log.e("DeleteDebug", it.message.toString())
                }
        }
    }

    fun toggleScheduleMenu(scheduleId: Long) {
        _uiState.update { state ->
            val currentId = state.plannerState.expandedScheduleId
            val nextId = if (currentId == scheduleId) -1L else scheduleId
            state.copy(
                plannerState = state.plannerState.copy(expandedScheduleId = nextId)
            )
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(plannerState = state.plannerState.copy(selectedDate = date))
        }
        updateFilteredSchedules()
    }

    fun createTodo(studyId: Long, content: String) {
        val selectedDate = uiState.value.plannerState.selectedDate
        if (selectedDate.isBefore(LocalDate.now())) return

        viewModelScope.launch {
            studyRepository.createTodo(
                studyId = studyId,
                content = content,
                dueDate = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            ).onSuccess { newTodoId ->
                _uiState.update { state ->
                    val newTodo = TodoModel(
                        id = newTodoId,
                        memberId = currentUserId,
                        content = content,
                        isCompleted = false
                    )
                    state.copy(
                        plannerState = state.plannerState.copy(
                            todoList = (state.plannerState.todoList + newTodo).toPersistentList()
                        )
                    )
                }
            }.onFailure { emitError(it) }
        }
    }

    fun toggleTodoStatus(studyId: Long, todoId: Long, isCurrentlyCompleted: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlyCompleted) {
                studyRepository.uncompleteTodo(studyId, todoId)
            } else {
                studyRepository.completeTodo(studyId, todoId)
            }

            result.onSuccess {
                _uiState.update { state ->
                    val newList = state.plannerState.todoList.map { todo ->
                        if (todo.id == todoId) todo.copy(isCompleted = !isCurrentlyCompleted)
                        else todo
                    }.toPersistentList()
                    state.copy(plannerState = state.plannerState.copy(todoList = newList))
                }
            }
        }
    }

    fun deleteTodo(studyId: Long, todoId: Long) {
        viewModelScope.launch {
            studyRepository.deleteTodo(studyId, todoId).onSuccess {
                _uiState.update { state ->
                    val updatedTodo = state.plannerState.todoList
                        .filterNot { it.id == todoId }
                        .toPersistentList()
                    state.copy(plannerState = state.plannerState.copy(todoList = updatedTodo))
                }
            }.onFailure { emitError(it) }
        }
    }

    fun fetchMemberTodos(studyId: Long, memberId: Long, date: LocalDate) {
        viewModelScope.launch {
            studyRepository.getMemberTodos(studyId, memberId, date.toString())
                .onSuccess { todoList ->
                    _uiState.update { state ->
                        state.copy(
                            plannerState = state.plannerState.copy(
                                selectedMemberId = memberId.toString(),
                                selectedDate = date,
                                todoList = todoList.toPersistentList()
                            )
                        )
                    }
                }
        }
    }

    fun postMemoir(
        studyId: Long,
        activity: String,
        learned: String,
        encouragement: String,
        isPrivate: Boolean,
        imageFiles: List<File>
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val memoirModel = MemoirCreateModel(activity, learned, encouragement, isPrivate)
            studyRepository.postMemoir(studyId, memoirModel, imageFiles)
                .onSuccess {
                    _sideEffect.emit(StudyDetailSideEffect.MemoirPostSuccess)
                    fetchStudyHomeDetail(studyId)
                }
                .onFailure { emitError(it) }
            _uiState.update { it.copy(isLoading = false) }
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
        updateMemoirUIState(memoirId, reactionType, !isCurrentlySelected) // ✅ 즉시 반영
        viewModelScope.launch {

            val result = if (isCurrentlySelected) {
                studyRepository.deleteReviewReaction(studyId, memoirId, reactionType)
            } else {
                studyRepository.postReviewReaction(studyId, memoirId, reactionType)
            }

            result.onFailure {
                updateMemoirUIState(memoirId, reactionType, isCurrentlySelected)
                emitError(it)
            }
        }
    }



    private fun updateMemoirUIState(memoirId: Long, reactionType: String, isSelected: Boolean) {
        _uiState.update { state ->
            val updatedMemoirs = state.memoirState.memoirs.map { memoir ->
                if (memoir.memoirId == memoirId) {
                    val diff = if (isSelected) 1 else -1

                    // 1. reactions 객체를 새로 생성
                    val newReactions = when (reactionType) {
                        "FIRE" -> memoir.reactions.copy(isFired = isSelected)
                        "HEART" -> memoir.reactions.copy(isHearted = isSelected)
                        "STAR" -> memoir.reactions.copy(isStarred = isSelected)
                        "SMILE" -> memoir.reactions.copy(isSmiled = isSelected)
                        else -> memoir.reactions
                    }

                    // 2. reactionCounts 객체를 새로 생성
                    val newCounts = when (reactionType) {
                        "FIRE" -> memoir.reactionCounts.copy(fireCount = (memoir.reactionCounts.fireCount + diff).coerceAtLeast(0))
                        "HEART" -> memoir.reactionCounts.copy(heartCount = (memoir.reactionCounts.heartCount + diff).coerceAtLeast(0))
                        "STAR" -> memoir.reactionCounts.copy(starCount = (memoir.reactionCounts.starCount + diff).coerceAtLeast(0))
                        "SMILE" -> memoir.reactionCounts.copy(smileCount = (memoir.reactionCounts.smileCount + diff).coerceAtLeast(0))
                        else -> memoir.reactionCounts
                    }

                    // 3. 최상위 memoir 객체를 새로 생성하여 리스트에 교체
                    memoir.copy(
                        reactions = newReactions,
                        reactionCounts = newCounts
                    )
                } else memoir
            }.toPersistentList()

            state.copy(memoirState = state.memoirState.copy(memoirs = updatedMemoirs))
        }
    }

    fun fetchAllMemoirs(studyId: Long, cursor: Long? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (currentUserId.isEmpty()) {
                currentUserId = tokenRepository.getUserId()
            }

            studyRepository.getFullStudyMemoirs(studyId, cursor, 20)
                .onSuccess { memoirs ->
                    val processedMemoirs = memoirs.map { memoir ->
                        memoir.copy(isMyMemoir = memoir.memberId.toString() == currentUserId)
                    }

                    _uiState.update { state ->
                        val currentList = if (cursor == null) emptyList() else state.memoirState.memoirs
                        state.copy(
                            isLoading = false,
                            memoirState = state.memoirState.copy(
                                memoirs = (currentList + processedMemoirs).toPersistentList()
                            )
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    emitError(it)
                }
        }
    }

    fun deleteMemoir(studyId: Long, memoirId: Long) {
        viewModelScope.launch {
            studyRepository.deleteMemoir(studyId, memoirId)
                .onSuccess {
                    _uiState.update { state ->
                        val updatedList = state.memoirState.memoirs
                            .filterNot { it.memoirId == memoirId }
                            .toPersistentList()

                        state.copy(
                            memoirState = state.memoirState.copy(memoirs = updatedList)
                        )
                    }
                }
                .onFailure { emitError(it) }
        }
    }

    private suspend fun emitError(t: Throwable) {
        _sideEffect.emit(StudyDetailSideEffect.ShowSnackBar(t.message ?: "오류가 발생했습니다."))
    }
}