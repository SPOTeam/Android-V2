package com.umcspot.spot.category.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.category.CategoryRoute
import com.umcspot.spot.category.CategoryViewModel
import com.umcspot.spot.category.screen.CategoryFilterScreen
import com.umcspot.spot.navigation.MainTabRoute
import com.umcspot.spot.navigation.routeNavigation
import kotlinx.serialization.Serializable

@Serializable
sealed class CategoryGraph : MainTabRoute {
    @Serializable
    data object Category : CategoryGraph()

    @Serializable
    data object CategoryFilter : CategoryGraph()
}

fun NavController.navigateToCategory(navOptions: NavOptions? = null) {
    navigate(CategoryGraph.Category, navOptions)
}

fun NavController.navigateToCategoryFilter(navOptions: NavOptions? = null) {
    navigate(CategoryGraph.CategoryFilter, navOptions)
}

fun NavGraphBuilder.categoryGraph(
    navController: NavController,
    contentPadding: PaddingValues,
    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
    onItemClick: (Long) -> Unit,
    onFilterClick: () -> Unit,
    onRegisterStudyClick: () -> Unit,
    navigateUp: () -> Unit
) {
    routeNavigation<CategoryGraph, CategoryGraph.Category> {
        composable<CategoryGraph.Category> {
            CategoryRoute(
                contentPadding = contentPadding,
                onRegisterScrollToTop = onRegisterScrollToTop,
                onItemClick = onItemClick,
                onFilterClick = onFilterClick,
                onRegisterStudyClick = onRegisterStudyClick
            )
        }

        composable<CategoryGraph.CategoryFilter> {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(CategoryGraph.Category)
            }
            val categoryViewModel: CategoryViewModel = hiltViewModel(parentEntry)
            CategoryFilterScreen(
                contentPadding = contentPadding,
                categoryViewModel = categoryViewModel,
                onAcceptFilterClick = navigateUp
            )
        }
    }
}