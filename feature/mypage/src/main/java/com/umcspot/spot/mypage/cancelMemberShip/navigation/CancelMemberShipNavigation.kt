package com.umcspot.spot.mypage.cancelMemberShip.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.mypage.cancelMemberShip.CancelMemberShipScreen
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToCancelMembership(navOptions: NavOptions? = null) {
    navigate(CancelMemberShip, navOptions)
}

fun NavGraphBuilder.cancelMemberShipGraph(
    contentPadding : PaddingValues,
    onCancelMembershipClick : () -> Unit,
) {
    composable<CancelMemberShip> {
        CancelMemberShipScreen(
            contentPadding = contentPadding,
            onCancelMembershipClick = onCancelMembershipClick
        )
    }
}

@Serializable
data object CancelMemberShip : MainTabRoute