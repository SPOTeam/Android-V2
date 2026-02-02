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
import com.umcspot.spot.study.model.StudyResult
import kotlinx.serialization.Serializable


fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(Home, navOptions)
}

fun NavGraphBuilder.homeGraph(
    contentPadding: PaddingValues,
    onQuickMenuClick: (QuickMenuType) -> Unit,
    onPopularClick: () -> Unit,
    onPopularPostClick: (Long) -> Unit,
    onStudyClick: (StudyResult) -> Unit,
    onStudyMoreClick: () -> Unit
) {
    composable<Home> {
        HomeScreen(
            contentPadding = contentPadding,
            onQuickMenuClick = onQuickMenuClick,
            onPopularClick = onPopularClick,
            onPopularPostClick = onPopularPostClick,
            onStudyClick = onStudyClick,
            onStudyMoreClick = onStudyMoreClick,
        )
    }
}

@Serializable
data object Home : MainTabRoute