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
import com.umcspot.spot.study.detail.screen.StudyMemoirPostRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToStudyDetail(studyId: Long, navOptions: NavOptions? = null) {
    navigate(StudyDetail(studyId), navOptions)
}

fun NavController.navigateToStudyMemoirPost(studyId: Long, navOptions: NavOptions? = null) {
    navigate(StudyMemoirPost(studyId), navOptions)
}

fun NavGraphBuilder.studyDetailGraph(
    contentPadding: PaddingValues,
    onDetailBackClick: () -> Unit,
    onMemoirPostBackClick: () -> Unit,
    onTabChanged: (StudyDetailTab) -> Unit,
    currentTab: StudyDetailTab,
    onOpenScheduleBottomSheet: () -> Unit,
    onDismissBottomSheet: () -> Unit
) {
    composable<StudyDetail> { backStackEntry ->
        val detail = backStackEntry.toRoute<StudyDetail>()
        StudyDetailRoute(
            contentPadding = contentPadding,
            studyId = detail.studyId,
            onBackClick = onDetailBackClick,
            onTabChanged = onTabChanged,
            initialTab = currentTab,
            onOpenScheduleBottomSheet = onOpenScheduleBottomSheet,
            onDismissBottomSheet = onDismissBottomSheet
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
}

@Serializable
data class StudyDetail(val studyId: Long) : Route

@Serializable
data class StudyMemoirPost(val studyId: Long) : Route