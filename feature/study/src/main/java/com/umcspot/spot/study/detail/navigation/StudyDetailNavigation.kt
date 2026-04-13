package com.umcspot.spot.study.detail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.study.detail.StudyDetailRoute
import com.umcspot.spot.study.detail.model.StudyDetailTab
import com.umcspot.spot.study.detail.screen.StudyBoardPostRoute
import com.umcspot.spot.study.detail.screen.StudyPostContentScreen
import com.umcspot.spot.study.detail.screen.attendance.StudyAttendanceRoute
import com.umcspot.spot.study.detail.screen.post.StudyMemoirPostRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToStudyDetail(studyId: Long, navOptions: NavOptions? = null) {
    navigate(StudyDetail(studyId), navOptions)
}

fun NavController.navigateToStudyMemoirPost(studyId: Long, navOptions: NavOptions? = null) {
    navigate(StudyMemoirPost(studyId), navOptions)
}

fun NavController.navigateToStudyBoardPost(studyId: Long, navOptions: NavOptions? = null) {
    navigate(StudyBoardPost(studyId), navOptions)
}

fun NavController.navigateToStudyAttendance(studyId: Long, scheduleId: Long, navOptions: NavOptions? = null) {
    navigate(StudyAttendance(studyId, scheduleId), navOptions)
}

fun NavGraphBuilder.studyDetailGraph(
    contentPadding: PaddingValues,
    onDetailBackClick: () -> Unit,
    onMemoirPostBackClick: () -> Unit,
    onBoardPostBackClick: () -> Unit,
    onPostContentBackClick: () -> Unit,
    onBoardPostClick: (Long, Long) -> Unit = { _, _ -> },
    onAttendanceBackClick: () -> Unit,
    onAttendanceClick: (Long, Long) -> Unit,
    onTabChanged: (StudyDetailTab) -> Unit,
    currentTab: StudyDetailTab,
) {
    composable<StudyDetail> { backStackEntry ->
        val detail = backStackEntry.toRoute<StudyDetail>()
        StudyDetailRoute(
            contentPadding = contentPadding,
            studyId = detail.studyId,
            onBackClick = onDetailBackClick,
            onTabChanged = onTabChanged,
            initialTab = currentTab,
            onBoardPostClick = { postId -> onBoardPostClick(detail.studyId, postId) },
            onAttendanceClick = { schedId -> onAttendanceClick(detail.studyId, schedId) }
        )
    }

    composable<StudyMemoirPost> { backStackEntry ->
        val post = backStackEntry.toRoute<StudyMemoirPost>()
        StudyMemoirPostRoute(
            studyId = post.studyId,
            contentPadding = contentPadding,
            onBackClick = onMemoirPostBackClick
        )
    }

    composable<StudyBoardPost> { backStackEntry ->
        val post = backStackEntry.toRoute<StudyBoardPost>()
        StudyBoardPostRoute(
            studyId = post.studyId,
            contentPadding = contentPadding,
            onBackClick = onBoardPostBackClick
        )
    }

    composable<StudyAttendance> { backStackEntry ->
        val args = backStackEntry.toRoute<StudyAttendance>()
        StudyAttendanceRoute(
            studyId = args.studyId,
            scheduleId = args.scheduleId,
            contentPadding = contentPadding,
            onBackClick = onAttendanceBackClick
        )
    }

    composable<StudyPostContent> { backStackEntry ->
        val args = backStackEntry.toRoute<StudyPostContent>()
        StudyPostContentScreen(
            contentPadding = contentPadding,
            studyId = args.studyId,
            postId = args.postId,
            onDeleteClick = onPostContentBackClick,
            onEditClick = {},
        )
    }
}

@Serializable
data class StudyDetail(val studyId: Long) : Route

@Serializable
data class StudyMemoirPost(val studyId: Long) : Route

@Serializable
data class StudyBoardPost(val studyId: Long) : Route

@Serializable
data class StudyAttendance(val studyId: Long, val scheduleId: Long) : Route

@Serializable
data class StudyPostContent(val studyId: Long, val postId: Long) : Route
