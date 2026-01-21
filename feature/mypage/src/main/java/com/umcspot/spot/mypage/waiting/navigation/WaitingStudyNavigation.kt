package com.umcspot.spot.mypage.waiting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.mypage.waiting.WaitingStudyScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToWaitingStudy(navOptions: NavOptions? = null) {
    navigate(WaitingStudy, navOptions)
}

fun NavGraphBuilder.waitingStudyGraph(
    contentPadding : PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onStudyClick : (Long) -> Unit,
    moveToRecruitingStudy :() -> Unit,
) {
    composable<WaitingStudy> {
        WaitingStudyScreen(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onStudyClick = onStudyClick,
            moveToRecruitingStudy = moveToRecruitingStudy,
        )
    }
}

@Serializable
data object WaitingStudy : Route