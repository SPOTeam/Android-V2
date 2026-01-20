package com.umcspot.spot.category.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.category.CategoryScreen
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToCategory(navOptions: NavOptions? = null) {
    navigate(Category, navOptions)
}

fun NavGraphBuilder.categoryGraph(
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onItemClick: (Long) -> Unit,
    onFilterClick: () -> Unit
) {
    composable<Category> {
        CategoryScreen(
            contentPadding = contentPadding,
            onRegisterScrollToTop = onRegisterScrollToTop,
            onItemClick = onItemClick,
            onFilterClick = onFilterClick
        )
    }
}

@Serializable
data object Category : MainTabRoute