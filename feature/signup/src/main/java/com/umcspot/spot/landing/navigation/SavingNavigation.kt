package com.umcspot.spot.landing.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.landing.SavingScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToSaving(navOptions: NavOptions? = null) {
    navigate(Saving, navOptions)
}

fun NavGraphBuilder.savingGraph(
    contentPadding : PaddingValues,
    onFinished : () -> Unit
) {
    composable<Saving> {
        SavingScreen(
            contentPadding = contentPadding,
            onFinished = onFinished
        )
    }
}

@Serializable
data object Saving : Route