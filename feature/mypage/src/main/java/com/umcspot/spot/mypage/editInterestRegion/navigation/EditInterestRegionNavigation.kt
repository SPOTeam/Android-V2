package com.umcspot.spot.mypage.editInterestRegion.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.mypage.editInterestRegion.EditInterestRegionScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToEditInterestRegion(navOptions: NavOptions? = null) {
    navigate(EditRegion, navOptions)
}

fun NavGraphBuilder.interestRegionGraph(
    contentPadding: PaddingValues,
    moveToMyInterestStudy: () -> Unit,
    moveToMyPage: () -> Unit
) {
    composable<EditRegion> {
        EditInterestRegionScreen(
            contentPadding = contentPadding,
            moveToMyInterestStudy = moveToMyInterestStudy,
            moveToMyPage = moveToMyPage
        )
    }
}

@Serializable
data object EditRegion : Route