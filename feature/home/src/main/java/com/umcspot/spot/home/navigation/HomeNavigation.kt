package com.umcspot.spot.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.home.HomeScreen
import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable


fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(Home, navOptions)
}

fun NavGraphBuilder.homeGraph(
    contentPadding: PaddingValues,
    onQuickMenuClick: (QuickMenuType) -> Unit
) {
    composable<Home> {
        HomeScreen(
            contentPadding = contentPadding,
            onQuickMenuClick = onQuickMenuClick
        )
    }
}

@Serializable
data object Home : MainTabRoute