package com.umcspot.spot.study.preferCategory.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.MainTabRoute
import com.umcspot.spot.study.preferCategory.PreferCategoryStudyScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToPreferCategoryStudy(navOptions: NavOptions? = null) {
    navigate(PreferCategory, navOptions)
}

fun NavGraphBuilder.preferCategoryStudyGraph(
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onItemClick: (Long) -> Unit,
    onFilterClick: () -> Unit
) {
    composable<PreferCategory> {
        PreferCategoryStudyScreen(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onItemClick = onItemClick,
            onFilterClick = onFilterClick
        )
    }
}

@Serializable
data object PreferCategory : MainTabRoute