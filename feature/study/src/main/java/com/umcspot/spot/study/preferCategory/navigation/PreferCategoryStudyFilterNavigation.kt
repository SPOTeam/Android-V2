package com.umcspot.spot.study.preferCategory.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.MainTabRoute
import com.umcspot.spot.study.preferCategory.PreferCategoryStudyFilterScreen
import com.umcspot.spot.study.preferCategory.PreferCategoryStudyViewModel
import kotlinx.serialization.Serializable

fun NavController.navigateToPreferCategoryStudyFilter(navOptions: NavOptions? = null) {
    navigate(PreferCategoryFilter, navOptions)
}

fun NavGraphBuilder.preferCategoryStudyFilterGraph(
    navController: NavController,
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
) {
    composable<PreferCategoryFilter> {
        val parentEntry = remember(navController) {
            navController.getBackStackEntry(PreferCategory)
        }

        val categoryVm: PreferCategoryStudyViewModel = hiltViewModel(parentEntry)
        PreferCategoryStudyFilterScreen(
            contentPadding = contentPadding,
            categoryVm = categoryVm,
            onAcceptFilterClick = onAcceptFilterClick
        )
    }
}

@Serializable
data object PreferCategoryFilter : MainTabRoute