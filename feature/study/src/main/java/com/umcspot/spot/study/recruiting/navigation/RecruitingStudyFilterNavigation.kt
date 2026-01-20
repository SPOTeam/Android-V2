package com.umcspot.spot.study.recruiting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.study.recruiting.RecruitingStudyFilterScreen
import com.umcspot.spot.study.recruiting.RecruitingStudyViewModel
import kotlinx.serialization.Serializable

fun NavController.navigateToRecruitingStudyFilter(navOptions: NavOptions? = null) {
    navigate(RecruitingFilter, navOptions)
}

fun NavGraphBuilder.recruitingStudyFilterGraph(
    navController: NavController,
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
) {
    composable<RecruitingFilter> {
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(Recruiting)
        }

        val recruitingVm: RecruitingStudyViewModel = hiltViewModel(parentEntry)

        RecruitingStudyFilterScreen(
            contentPadding = contentPadding,
            onAcceptFilterClick = onAcceptFilterClick,
            viewModel = recruitingVm
        )
    }
}
@Serializable
data object RecruitingFilter : Route
