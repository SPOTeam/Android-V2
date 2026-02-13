package com.umcspot.spot.study.detail.model

import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyPostResult
import com.umcspot.spot.study.model.StudyPostsResultList
import com.umcspot.spot.study.model.StudyRecentMemoirModel
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

data class StudyDetailState(
    val homeState: StudyHomeState = StudyHomeState(),
    val plannerState: StudyPlannerState = StudyPlannerState(),
    val postState : StudyPostState = StudyPostState(),
    val memoirState: StudyMemoirState = StudyMemoirState(),
    val isLoading: Boolean = false
)

data class StudyHomeState(
    val studyTitle: String = "",
    val studyDescription: String = "",
    val thumbnailUrl: String? = null,
    val categories: ImmutableList<String> = persistentListOf(),
    val currentMembers: Int = 0,
    val totalMembers: Int = 0,
    val likeCount: Int = 0,
    val hitCount: Int = 0,
    val members: ImmutableList<StudyMemberModel> = persistentListOf(),
    val schedules: ImmutableList<StudyScheduleModel> = persistentListOf(),
    val recentMemoirs: ImmutableList<StudyRecentMemoirModel> = persistentListOf()
)

data class StudyPlannerState(
    val selectedDate: LocalDate = LocalDate.now(),
    val monthlySchedules: ImmutableList<StudyScheduleModel> = persistentListOf(),
    val selectedDaySchedules: ImmutableList<StudyScheduleModel> = persistentListOf(),
    val todoList: ImmutableList<TodoModel> = persistentListOf(),
    val selectedMemberId: String = ""
)

data class StudyPostState(
    val studyPosts: ImmutableList<StudyPostResult> = persistentListOf(),
    val hasNext: Boolean = false,
    val nextCursor: Long? = null
)

data class StudyMemoirState(
    val memoirs: ImmutableList<MemoirModel> = persistentListOf(),
    val hasNext: Boolean = false,
    val nextCursor: Long = 0L,
    val totalElements: Int = 0
)

sealed interface StudyDetailSideEffect {
    data class ShowSnackBar(val message: String) : StudyDetailSideEffect
    object MemoirPostSuccess : StudyDetailSideEffect
    object BoardPostSuccess : StudyDetailSideEffect
    object ReactionSuccess : StudyDetailSideEffect
}