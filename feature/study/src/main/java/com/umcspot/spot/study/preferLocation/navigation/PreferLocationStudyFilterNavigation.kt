//package com.umcspot.spot.study.preferLocation.navigation
//
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.navigation.NavController
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavOptions
//import androidx.navigation.compose.composable
//import com.umcspot.spot.navigation.Route
//import com.umcspot.spot.study.preferLocation.PreferLocationStudyFilterScreen
//import kotlinx.serialization.Serializable
//
//fun NavController.navigateToPreferLocationStudyFilter(navOptions: NavOptions? = null) {
//    navigate(PreferLocationFilter, navOptions)
//}
//
//fun NavGraphBuilder.preferLocationStudyFilterGraph(
//    contentPadding: PaddingValues,
//    onAcceptFilterClick: () -> Unit,
//) {
//    composable<PreferLocationFilter> {
//        PreferLocationStudyFilterScreen(
//            contentPadding = contentPadding,
//            onAcceptFilterClick = onAcceptFilterClick,
//        )
//    }
//}
//@Serializable
//data object PreferLocationFilter : Route
