package com.umcspot.spot.mypage.participating.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.mypage.participating.ParticipatingScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToParticipatingStudy(navOptions: NavOptions? = null) {
    navigate(ParticipatingStudy, navOptions)
}

fun NavGraphBuilder.participatingGraph(
    contentPadding : PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onStudyClick : (Long) -> Unit,
    moveToRecruitingStudy : () -> Unit
) {
    composable<ParticipatingStudy> {
        ParticipatingScreen(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onStudyClick = onStudyClick,
            moveToRecruitingStudy = moveToRecruitingStudy
        )
    }
}

@Serializable
data object ParticipatingStudy : Route