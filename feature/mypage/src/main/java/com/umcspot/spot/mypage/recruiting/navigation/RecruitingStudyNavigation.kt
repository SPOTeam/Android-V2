package com.umcspot.spot.mypage.recruiting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.mypage.recruiting.RecruitingStudyScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToMyRecruitingStudy(navOptions: NavOptions? = null) {
    navigate(MyRecruitingStudy, navOptions)
}

fun NavGraphBuilder.myRecruitingStudyGraph(
    contentPadding : PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onStudyClick : (Long) -> Unit,
    moveToMakeStudy :() -> Unit,
    moveToCheckApplied: (Long) -> Unit
) {
    composable<MyRecruitingStudy> {
        RecruitingStudyScreen(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onStudyClick = onStudyClick,
            moveToMakeStudy = moveToMakeStudy,
            moveToCheckApplied = moveToCheckApplied
        )
    }
}

@Serializable
data object MyRecruitingStudy : Route