package com.umcspot.spot.study.recruiting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.study.recruiting.RecruitingStudyScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToRecruitingStudy(navOptions: NavOptions? = null) {
    navigate(Recruiting, navOptions)
}

fun NavGraphBuilder.recruitingStudyGraph(
    contentPadding : PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onFilterClick : () -> Unit,
    onItemClick : (Long) -> Unit
) {
    composable<Recruiting> {
        RecruitingStudyScreen(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onFilterClick = onFilterClick,
            onItemClick = onItemClick
        )
    }
}

@Serializable
data object Recruiting : Route