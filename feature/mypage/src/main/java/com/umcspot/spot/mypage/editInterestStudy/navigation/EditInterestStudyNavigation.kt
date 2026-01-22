package com.umcspot.spot.mypage.editInterestStudy.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.mypage.cancelMemberShip.CancelMemberShipScreen
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToEditInterestStudy(navOptions: NavOptions? = null) {
    navigate(InterestStudy, navOptions)
}

fun NavGraphBuilder.interestStudyGraph(
    contentPadding : PaddingValues,
    moveToMyInterestStudy: () -> Unit,
) {
    composable<InterestStudy> {
        EditInterstStudyScreen(
            contentPadding = contentPadding,
            moveToMyInterestStudy = moveToMyInterestStudy
        )
    }
}

@Serializable
data object InterestStudy : MainTabRoute