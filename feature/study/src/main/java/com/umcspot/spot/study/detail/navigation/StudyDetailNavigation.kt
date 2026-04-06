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
import com.umcspot.spot.study.detail.screen.attendance.StudyAttendanceRoute
import com.umcspot.spot.study.detail.screen.post.StudyMemoirPostRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToStudyDetail(studyId: Long, navOptions: NavOptions? = null) {
    navigate(StudyDetail(studyId), navOptions)
}

fun NavController.navigateToStudyMemoirPost(studyId: Long, navOptions: NavOptions? = null) {
    navigate(StudyMemoirPost(studyId), navOptions)
}

fun NavController.navigateToStudyAttendance(studyId: Long, scheduleId: Long, navOptions: NavOptions? = null) {
    navigate(StudyAttendance(studyId, scheduleId), navOptions)
}

fun NavGraphBuilder.studyDetailGraph(
    contentPadding: PaddingValues,
    onDetailBackClick: () -> Unit,
    onMemoirPostBackClick: () -> Unit,
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

    composable<StudyAttendance> { backStackEntry ->
        val args = backStackEntry.toRoute<StudyAttendance>()
        StudyAttendanceRoute(
            studyId = args.studyId,
            scheduleId = args.scheduleId,
            contentPadding = contentPadding,
            onBackClick = onAttendanceBackClick
        )
    }
}

@Serializable
data class StudyDetail(val studyId: Long) : Route

@Serializable
data class StudyMemoirPost(val studyId: Long) : Route

@Serializable
data class StudyAttendance(val studyId: Long, val scheduleId: Long) : Route