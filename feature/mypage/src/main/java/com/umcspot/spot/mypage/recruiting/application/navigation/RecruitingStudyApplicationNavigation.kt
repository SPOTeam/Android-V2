package com.umcspot.spot.mypage.recruiting.application.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.umcspot.spot.mypage.recruiting.application.RecruitingStudyRequestScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToStudyApplications(studyId: Long) {
    navigate("studyApplication/$studyId")
}
fun NavGraphBuilder.recruitingStudyApplicationsGraph(
    contentPadding : PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
) {
    composable(
        route = STUDY_APPLICATION_ROUTE,
        arguments = listOf(navArgument("studyId") { type = NavType.LongType })
    ) { backStackEntry ->
        val studyId = backStackEntry.arguments?.getLong("studyId") ?: return@composable

        RecruitingStudyRequestScreen(
            contentPadding = contentPadding,
            studyId = studyId,
            onRegisterScrollToTop = onRegisterScrollToTop,
        )
    }
}

@Serializable
const val STUDY_APPLICATION_ROUTE = "studyApplication/{studyId}"
