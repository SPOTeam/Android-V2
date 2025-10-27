package com.umcspot.spot.study.recruiting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.study.filter.RecruitingStudyFilterScreen
import com.umcspot.spot.study.model.StudyResult
import kotlinx.serialization.Serializable

fun NavController.navigateToRecruitingStudyFilter(navOptions: NavOptions? = null) {
    navigate(RecruitingFilter, navOptions)
}

fun NavGraphBuilder.recruitingStudyFilterGraph(
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
) {
    composable<RecruitingFilter> {
        RecruitingStudyFilterScreen(
            contentPadding = contentPadding,
            onAcceptFilterClick = onAcceptFilterClick,
        )
    }
}
@Serializable
data object RecruitingFilter : Route
