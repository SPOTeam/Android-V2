package com.umcspot.spot.jjim.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.jjim.JJimScreen
import com.umcspot.spot.navigation.MainTabRoute
import com.umcspot.spot.study.model.StudyResult
import kotlinx.serialization.Serializable

fun NavController.navigateToJJim(navOptions: NavOptions? = null) {
    navigate(JJim, navOptions)
}

fun NavGraphBuilder.jjimGraph(
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onMoveToStudyClick: () -> Unit,
    onItemClick: (Long) -> Unit
) {
    composable<JJim> {
        JJimScreen(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onMoveToStudyClick = onMoveToStudyClick,
            onItemClick = onItemClick
        )
    }
}

@Serializable
data object JJim : MainTabRoute