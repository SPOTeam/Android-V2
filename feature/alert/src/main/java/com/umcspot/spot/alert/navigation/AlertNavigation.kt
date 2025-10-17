package com.umcspot.spot.alert.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.alert.AlertScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable


fun NavController.navigateToAlert(navOptions: NavOptions? = null) {
    navigate(Alert, navOptions)
}

fun NavGraphBuilder.alertGraph(
    contentPadding : PaddingValues
) {
    composable<Alert> {
        AlertScreen(contentPadding = contentPadding)
    }
}

@Serializable
data object Alert : Route