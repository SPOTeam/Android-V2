package com.umcspot.spot.alert.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.umcspot.spot.alert.AlertScreen
import com.umcspot.spot.alert.EnrolledAlertScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable


fun NavController.navigateToAlert(navOptions: NavOptions? = null) {
    navigate(Alert, navOptions)
}

fun NavController.navigateToAppliedAlert(navOptions: NavOptions? = null) {
    navigate(AppliedAlert, navOptions)
}

fun NavGraphBuilder.alertGraph(
    contentPadding : PaddingValues,
    onClickApplied : () -> Unit
) {
    composable<Alert> {
        AlertScreen(
            contentPadding = contentPadding,
            onClickApplied = onClickApplied
        )
    }
}

fun NavGraphBuilder.appliedAlertGraph(
    contentPadding : PaddingValues
) {
    composable<AppliedAlert> {
        EnrolledAlertScreen(contentPadding = contentPadding)
    }
}

@Serializable
data object Alert : Route

@Serializable
data object AppliedAlert : Route