package com.umcspot.spot.jjim.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.jjim.JJimScreen
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToJJim(navOptions: NavOptions? = null) {
    navigate(JJim, navOptions)
}

fun NavGraphBuilder.jjimGraph() {
    composable<JJim> {
        JJimScreen()
    }
}

@Serializable
data object JJim : MainTabRoute