package com.umcspot.spot.study.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.study.detail.model.StudyDetailSideEffect
import com.umcspot.spot.study.detail.model.StudyDetailState
import com.umcspot.spot.study.model.MemoirCreateModel
import com.umcspot.spot.study.model.TodoModel
import com.umcspot.spot.study.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class StudyDetailViewModel @Inject constructor(
    private val studyRepository: StudyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyDetailState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<StudyDetailSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

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
                _uiState.update { it.copy(homeState = it.homeState.copy(schedules = schedules.toPersistentList())) }
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
                    val updatedPlanner = state.plannerState.copy(
                        monthlySchedules = schedules.toPersistentList()
                    )
                    state.copy(plannerState = updatedPlanner)
                }
                updateFilteredSchedules()
            }
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(plannerState = state.plannerState.copy(selectedDate = date))
        }
        updateFilteredSchedules()
    }

    private fun updateFilteredSchedules() {
        _uiState.update { state ->
            val date = state.plannerState.selectedDate
            // 필터링: 시작일과 종료일 사이에 선택한 날짜가 있는지 확인
            val filtered = state.plannerState.monthlySchedules.filter { schedule ->
                val start = schedule.startAt.toLocalDate()
                val end = schedule.endAt.toLocalDate()
                !date.isBefore(start) && !date.isAfter(end)
            }.toPersistentList() // 우선 .take(2)를 지우고 다 나오는지 확인하세요!

            // 로그를 찍어서 필터링이 되는지 꼭 확인하세요
            Log.d("PlannerDebug", "선택날짜: $date, 전체개수: ${state.plannerState.monthlySchedules.size}, 필터후: ${filtered.size}")

            state.copy(plannerState = state.plannerState.copy(selectedDaySchedules = filtered))
        }
    }

    fun createTodo(studyId: Long, content: String) {
        val selectedDate = uiState.value.plannerState.selectedDate
        val today = LocalDate.now()

        if (selectedDate.isBefore(today)) return

        viewModelScope.launch {
            studyRepository.createTodo(
                studyId = studyId,
                content = content,
                dueDate = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            ).onSuccess { newTodoId ->
                _uiState.update { state ->
                    val newTodo = TodoModel(
                        id = newTodoId,
                        memberId = state.plannerState.selectedMemberId,
                        content = content,
                        isCompleted = false
                    )
                    state.copy(
                        plannerState = state.plannerState.copy(
                            todoList = (state.plannerState.todoList + newTodo).toPersistentList()
                        )
                    )
                }
            }.onFailure {
                emitError(it)
            }
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

            val memoirModel = MemoirCreateModel(
                activity = activity,
                learned = learned,
                encouragement = encouragement,
                isPrivate = isPrivate
            )

            studyRepository.postMemoir(studyId, memoirModel, imageFiles)
                .onSuccess {
                    _sideEffect.emit(StudyDetailSideEffect.MemoirPostSuccess)
                    fetchStudyHomeDetail(studyId)
                }
                .onFailure { emitError(it) }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun toggleMemoirReaction(
        studyId: Long,
        memoirId: Long,
        reactionType: String,
        isCurrentlySelected: Boolean
    ) {
        viewModelScope.launch {
            // 1. 낙관적 업데이트
            updateMemoirUIState(memoirId, reactionType, !isCurrentlySelected)

            // 2. 서버 통신 (Repository API 호출)
            val result = if (isCurrentlySelected) {
                studyRepository.deleteReviewReaction(studyId, memoirId, reactionType)
            } else {
                studyRepository.postReviewReaction(studyId, memoirId, reactionType)
            }

            // 3. 실패 시 롤백
            result.onFailure { error ->
                updateMemoirUIState(memoirId, reactionType, isCurrentlySelected)
                emitError(error)
            }
        }
    }

    private fun updateMemoirUIState(memoirId: Long, reactionType: String, isSelected: Boolean) {
        _uiState.update { state ->
            val updatedMemoirs = state.memoirState.memoirs.map { memoir ->
                if (memoir.memoirId == memoirId) {
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
                } else memoir
            }.toPersistentList()
            state.copy(memoirState = state.memoirState.copy(memoirs = updatedMemoirs))
        }
    }

    fun fetchAllMemoirs(studyId: Long, cursor: Long? = null) {
        viewModelScope.launch {
            // 나중에 실제 memberId
            val myMemberId = -1L

            studyRepository.getFullStudyMemoirs(studyId, cursor, 20).onSuccess { memoirs ->
                val processedMemoirs = memoirs.map { memoir ->
                    memoir.copy(isMyMemoir = memoir.memberId == myMemberId)
                }

                _uiState.update { state ->
                    val currentList = if (cursor == null) emptyList() else state.memoirState.memoirs
                    state.copy(
                        memoirState = state.memoirState.copy(
                            memoirs = (currentList + processedMemoirs).toPersistentList()
                        )
                    )
                }
            }.onFailure { emitError(it) }
        }
    }

    fun deleteMemoir(studyId: Long, memoirId: Long) {
        viewModelScope.launch {
            studyRepository.deleteMemoir(studyId, memoirId).onSuccess {
                _uiState.update { state ->
                    val updatedList = state.memoirState.memoirs
                        .filterNot { it.memoirId == memoirId }
                        .toPersistentList()
                    state.copy(memoirState = state.memoirState.copy(memoirs = updatedList))
                }
            }.onFailure { emitError(it) }
        }
    }

    fun createSchedule(
        studyId: Long,
        title: String,
        location: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            studyRepository.createSchedule(
                studyId = studyId,
                title = title,
                location = location,
                startAt = startAt,
                endAt = endAt
            ).onSuccess {
                _sideEffect.emit(StudyDetailSideEffect.ScheduleCreateSuccess)

                // 1. 플래너 탭 데이터 갱신 (캘린더용)
                val selectedDate = _uiState.value.plannerState.selectedDate
                fetchMonthlySchedules(
                    studyId = studyId,
                    year = selectedDate.year,
                    month = selectedDate.monthValue
                )

                // 2. 홈 탭 데이터 갱신 (다가오는 일정용) ★ 이 줄을 추가하세요!
                fetchStudyHomeDetail(studyId)

            }.onFailure { error ->
                emitError(error)
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun emitError(t: Throwable) {
        _sideEffect.emit(StudyDetailSideEffect.ShowSnackBar(t.message ?: "데이터를 불러오는데 실패했습니다."))
    }
}