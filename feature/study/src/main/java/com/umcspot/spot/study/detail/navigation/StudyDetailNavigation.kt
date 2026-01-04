package com.umcspot.spot.study.detail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.study.detail.StudyDetailRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToStudyDetail(studyId: Long, navOptions: NavOptions? = null) {
    navigate(StudyDetail(studyId), navOptions)
}

fun NavGraphBuilder.studyDetailGraph(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit
) {
    composable<StudyDetail> { backStackEntry ->
        val detail = backStackEntry.toRoute<StudyDetail>()

        StudyDetailRoute(
            contentPadding = contentPadding,
            studyId = detail.studyId,
            onBackClick = onBackClick
        )
    }
}

@Serializable
data class StudyDetail(val studyId: Long) : Route