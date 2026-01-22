package com.umcspot.spot.mypage.editInterestStudy.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.mypage.editInterestStudy.EditInterestStudyScreen
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToEditInterestStudy(navOptions: NavOptions? = null) {
    navigate(EditInterest, navOptions)
}

fun NavGraphBuilder.interestStudyGraph(
    contentPadding : PaddingValues,
    moveToMyInterestStudy: () -> Unit,
    moveToMyPage: () -> Unit
) {
    composable<EditInterest> {
        EditInterestStudyScreen(
            contentPadding = contentPadding,
            moveToMyInterestStudy = moveToMyInterestStudy,
            moveToMyPage = moveToMyPage
        )
    }
}

@Serializable
data object EditInterest : MainTabRoute