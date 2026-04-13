package com.umcspot.spot.jjim.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.jjim.JJimRoute
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToJJim(navOptions: NavOptions? = null) {
    navigate(JJim, navOptions)
}

fun NavGraphBuilder.jjimGraph(
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onItemClick: (Long) -> Unit
) {
    composable<JJim> {
        JJimRoute(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onItemClick = onItemClick
        )
    }
}

@Serializable
data object JJim : MainTabRoute