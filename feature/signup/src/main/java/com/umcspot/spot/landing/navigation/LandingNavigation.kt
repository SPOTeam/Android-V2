package com.umcspot.spot.landing.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.signup.landing.LandingScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToLanding(navOptions: NavOptions? = null) {
    navigate(Landing, navOptions)
}

fun NavGraphBuilder.landingGraph(
    onKakaoClick: () -> Unit,
    onNaverClick: () -> Unit
) {
    composable<Landing> {
        LandingScreen(
            onKakaoClick = onKakaoClick,
            onNaverClick = onNaverClick
        )
    }
}

@Serializable
data object Landing : Route