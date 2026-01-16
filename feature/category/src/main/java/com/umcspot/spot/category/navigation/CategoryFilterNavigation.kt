package com.umcspot.spot.category.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.category.CategoryFilterScreen
import com.umcspot.spot.category.CategoryScreen
import com.umcspot.spot.category.CategoryViewModel
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToCategoryFilter(navOptions: NavOptions? = null) {
    navigate(CategoryFilter, navOptions)
}

fun NavGraphBuilder.categoryFilterGraph(
    navController: NavController,
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
) {
    composable<CategoryFilter> {
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(Category)
        }

        val categoryVm: CategoryViewModel = hiltViewModel(parentEntry)
        CategoryFilterScreen(
            contentPadding = contentPadding,
            categoryVm = categoryVm,
            onAcceptFilterClick = onAcceptFilterClick
        )
    }
}

@Serializable
data object CategoryFilter : MainTabRoute