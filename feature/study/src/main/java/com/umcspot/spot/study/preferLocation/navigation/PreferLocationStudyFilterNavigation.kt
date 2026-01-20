package com.umcspot.spot.study.preferLocation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.study.preferLocation.PreferLocationStudyFilterScreen
import com.umcspot.spot.study.preferLocation.PreferLocationStudyViewModel
import kotlinx.serialization.Serializable

fun NavController.navigateToPreferLocationStudyFilter(navOptions: NavOptions? = null) {
    navigate(PreferLocationFilter, navOptions)
}

fun NavGraphBuilder.preferLocationStudyFilterGraph(
    navController: NavController,
    contentPadding: PaddingValues,
    onAcceptFilterClick: () -> Unit,
) {
    composable<PreferLocationFilter> {
        val parentEntry = remember(navController) {

            navController.getBackStackEntry(PreferLocation)
        }

        val preferLocationVm: PreferLocationStudyViewModel = hiltViewModel(parentEntry)

        PreferLocationStudyFilterScreen(
            contentPadding = contentPadding,
            onAcceptFilterClick = onAcceptFilterClick,
            preferLocationVm = preferLocationVm
        )
    }
}
@Serializable
data object PreferLocationFilter : Route
