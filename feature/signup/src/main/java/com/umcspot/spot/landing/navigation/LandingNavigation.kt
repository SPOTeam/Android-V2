package com.umcspot.spot.landing.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.landing.LandingRoute
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToLanding(navOptions: NavOptions? = null) {
    navigate(Landing, navOptions)
}

fun NavGraphBuilder.landingGraph(
    onLoginSuccess : () -> Unit
) {
    composable<Landing> {
        LandingRoute(
            onLoginSuccess = onLoginSuccess
        )
    }
}

@Serializable
data object Landing : Route