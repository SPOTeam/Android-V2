package com.umcspot.spot.study.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.study.detail.model.StudyDetailSideEffect
import com.umcspot.spot.study.detail.model.StudyDetailState
import com.umcspot.spot.study.model.BoardCreateModel
import com.umcspot.spot.study.model.MemoirCreateModel
import com.umcspot.spot.study.model.StudyAttendanceListModel
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import com.umcspot.spot.study.model.ViewerStatus
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.token.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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

            val currentPostCursor = uiState.value.postState.nextCursor

            val detailDeferred = async { studyRepository.getStudyDetail(studyId) }
            val membersDeferred = async { studyRepository.getStudyMembers(studyId) }
            val schedulesDeferred = async { studyRepository.getUpcomingSchedules(studyId) }
            val postsDeferred = async {
                studyRepository.getStudyPostsList(
                    studyId = studyId,
                    cursor = currentPostCursor,
                    size = 20
                )
            }
            val memoirsDeferred = async { studyRepository.getStudyRecentMemoirs(studyId) }

            detailDeferred.await().onSuccess { model ->
                _uiState.update { state ->
                    state.copy(
                        homeState = state.homeState.copy(
                            studyTitle = model.title,
                            studyDescription = model.description,
                            thumbnailUrl = model.thumbnailUrl,
                            categories = model.categories.toPersistentList(),
                            currentMembers = model.currentMembers,
                            totalMembers = model.totalMembers,
                            likeCount = model.likeCount,
                            hitCount = model.hitCount,
                            viewerStatus = model.viewerStatus,
                            isJoined = model.viewerStatus == ViewerStatus.APPROVED || model.viewerStatus == ViewerStatus.OWNER,
                            isHost = model.viewerStatus == ViewerStatus.OWNER
                        )
                    )
                }
            }.onFailure { emitError(it) }

            membersDeferred.await().onSuccess { members ->
                _uiState.update { state ->
                    state.copy(homeState = state.homeState.copy(members = members.toPersistentList()))
                }
            }.onFailure { emitError(it) }

            schedulesDeferred.await().onSuccess { schedules ->
                _uiState.update { it.copy(homeState = it.homeState.copy(schedules = schedules.toPersistentList())) }
            }.onFailure { emitError(it) }

            postsDeferred.await().onSuccess { posts ->
                _uiState.update { state ->
                    val currentList = if (currentPostCursor == null) emptyList() else state.postState.studyPosts
                    state.copy(
                        postState = state.postState.copy(
                            studyPosts = (currentList + posts.studyPostsList).toPersistentList(),
                            hasNext = posts.hasNext,
                            nextCursor = posts.nextCursor
                        )
                    )
                }
            }.onFailure { emitError(it) }

            memoirsDeferred.await().onSuccess { memoirs ->
                _uiState.update { it.copy(homeState = it.homeState.copy(recentMemoirs = memoirs.toPersistentList())) }
            }.onFailure { emitError(it) }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun applyStudy(studyId: Long, message: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            studyRepository.applyStudy(studyId, message)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            homeState = state.homeState.copy(viewerStatus = ViewerStatus.APPLIED)
                        )
                    }
                    _sideEffect.emit(StudyDetailSideEffect.ApplySuccess)
                }
                .onFailure { t ->
                    _uiState.update { it.copy(isLoading = false) }
                    emitError(t)
                }
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
        viewModelScope.launch {
            studyRepository.createSchedule(studyId, title, location, startAt, endAt)
                .onSuccess {
                    val date = _uiState.value.plannerState.selectedDate
                    fetchMonthlySchedules(studyId, date.year, date.monthValue)
                    fetchStudyHomeDetail(studyId)
                    _sideEffect.emit(StudyDetailSideEffect.ScheduleCreateSuccess)
                }
                .onFailure { t ->
                    val errorMessage = t.message ?: ""
                    if (errorMessage.contains("SCHEDULE4001") || errorMessage.contains("이미 일정이 존재")) {
                        _uiState.update { it.copy(plannerState = it.plannerState.copy(isOverlapError = true)) }
                    } else {
                        _uiState.update { it.copy(plannerState = it.plannerState.copy(isOverlapError = true)) }
                        emitError(t)
                    }
                }
        }
    }

    fun clearScheduleError() {
        _uiState.update { it.copy(plannerState = it.plannerState.copy(isOverlapError = false)) }
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

    fun fetchAttendanceList(studyId: Long, scheduleId: Long) {
        viewModelScope.launch {
            studyRepository.getAttendanceList(studyId, scheduleId)
                .onSuccess { model: StudyAttendanceListModel ->
                    _uiState.update { state ->
                        state.copy(
                            attendanceState = state.attendanceState.copy(
                                attendanceList = model.attendances.toPersistentList()
                            )
                        )
                    }
                }
                .onFailure { emitError(it) }
            }
        }

    fun refreshAttendanceStatus(studyId: Long, scheduleId: Long) {
        viewModelScope.launch {
            studyRepository.getAttendanceList(studyId, scheduleId)
                .onSuccess { model ->
                    _uiState.update { state ->
                        state.copy(
                            attendanceState = state.attendanceState.copy(
                                attendanceList = model.attendances.toPersistentList()
                            )
                        )
                    }
                }
        }
    }

    fun fetchAttendanceQr(studyId: Long, scheduleId: Long) {
        viewModelScope.launch {
            studyRepository.getAttendanceQr(studyId, scheduleId)
                .onSuccess { model ->
                    _uiState.update { state ->
                        state.copy(attendanceState = state.attendanceState.copy(
                            qrCodeImageUrl = model.qrCodeImageUrl,
                            isAttendanceActive = model.attendanceActive
                        ))
                    }
                }
                .onFailure { emitError(it) }
        }
    }

    fun fetchMembersOnly(studyId: Long) {
        viewModelScope.launch {
            studyRepository.getStudyMembers(studyId).onSuccess { members ->
                _uiState.update { it.copy(
                    homeState = it.homeState.copy(members = members.toPersistentList())
                )}
            }.onFailure { emitError(it) }
        }
    }

    fun startAttendance(studyId: Long, scheduleId: Long) {
        viewModelScope.launch {
            studyRepository.startAttendance(studyId, scheduleId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            attendanceState = state.attendanceState.copy(
                                isAttendanceActive = true
                            )
                        )
                    }
                    fetchAttendanceList(studyId, scheduleId)

                    repeat(5) { attempt ->
                        val qrResult = studyRepository.getAttendanceQr(studyId, scheduleId)
                        qrResult.onSuccess { model ->
                            _uiState.update { state ->
                                state.copy(
                                    attendanceState = state.attendanceState.copy(
                                        qrCodeImageUrl = model.qrCodeImageUrl
                                    )
                                )
                            }
                        }
                        if (_uiState.value.attendanceState.qrCodeImageUrl != null) return@repeat
                        delay(1000L * (attempt + 1))
                    }

                    if (_uiState.value.attendanceState.qrCodeImageUrl == null) {
                        emitError(Throwable("QR코드를 불러오지 못했습니다. 다시 시도해주세요."))
                    }
                }
                .onFailure { emitError(it) }
        }
    }

    fun finishAttendance(studyId: Long, scheduleId: Long) {
        viewModelScope.launch {
            studyRepository.finishAttendance(studyId, scheduleId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(attendanceState = state.attendanceState.copy(
                            isAttendanceActive = false,
                            qrCodeImageUrl = null
                        ))
                    }
                }
                .onFailure { emitError(it) }
        }
    }

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

    fun toggleMemoirReaction(studyId: Long, memoirId: Long, reactionType: String) {
        val memoir = _uiState.value.memoirState.memoirs.find { it.memoirId == memoirId } ?: return
        val isCurrentlySelected = when (reactionType) {
            "FIRE" -> memoir.reactions.isFired
            "HEART" -> memoir.reactions.isHearted
            "STAR" -> memoir.reactions.isStarred
            "SMILE" -> memoir.reactions.isSmiled
            else -> return
        }
    }

    fun postBoard(
        studyId: Long,
        title: String,
        content: String,
        isPrivate: Boolean,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val boardModel = BoardCreateModel(
                title = title,
                content = content,
                isPrivate = isPrivate
            )

            studyRepository.postBoard(studyId, boardModel)
                .onSuccess {
                    _sideEffect.emit(StudyDetailSideEffect.BoardPostSuccess)
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

    fun togglePostPin(studyId: Long, postId: Long, isCurrentlyPinned: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlyPinned) {
                studyRepository.studyPostUnPin(studyId, postId)
            } else {
                studyRepository.studyPostPin(studyId, postId)
            }

            result.onSuccess {
                _uiState.update { state ->
                    val updatedPosts = state.postState.studyPosts
                        .map { post ->
                            if (post.postId == postId) post.copy(isPinned = !isCurrentlyPinned) else post
                        }
                        .sortedByDescending { it.isPinned }
                        .toPersistentList()

                    state.copy(postState = state.postState.copy(studyPosts = updatedPosts))
                }
            }.onFailure { emitError(it) }
        }
    }

    fun togglePostLike(studyId: Long, postId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlyLiked) {
                studyRepository.studyPostUnLike(studyId, postId)
            } else {
                studyRepository.studyPostLike(studyId, postId)
            }

            result.onSuccess {
                _uiState.update { state ->
                    val updatedPosts = state.postState.studyPosts
                        .map { post ->
                            if (post.postId == postId) {
                                post.copy(
                                    isLiked = !isCurrentlyLiked,
                                    likeCount = if (!isCurrentlyLiked) post.likeCount + 1 else post.likeCount - 1
                                )
                            } else post
                        }
                        .toPersistentList()

                    state.copy(postState = state.postState.copy(studyPosts = updatedPosts))
                }
            }.onFailure { emitError(it) }
        }
    }

    private suspend fun emitError(t: Throwable) {
        _sideEffect.emit(StudyDetailSideEffect.ShowSnackBar(t.message ?: "오류가 발생했습니다."))
    }
}