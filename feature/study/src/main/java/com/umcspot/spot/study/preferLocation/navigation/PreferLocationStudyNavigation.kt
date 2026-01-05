//package com.umcspot.spot.study.preferLocation.navigation
//
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.navigation.NavController
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavOptions
//import androidx.navigation.compose.composable
//import com.umcspot.spot.navigation.Route
//import com.umcspot.spot.study.model.StudyResult
//import com.umcspot.spot.study.preferLocation.PreferLocationStudyScreen
//import kotlinx.serialization.Serializable
//
//fun NavController.navigateToPreferLocationStudy(navOptions: NavOptions? = null) {
//    navigate(PreferLocation, navOptions)
//}
//
//fun NavGraphBuilder.preferLocationStudyGraph(
//    contentPadding : PaddingValues,
//    onRegisterScrollToTop: ((() -> Unit)?) -> Unit,
//    onFilterClick : () -> Unit,
//    onItemClick : (StudyResult) -> Unit
//) {
//    composable<PreferLocation> {
//        PreferLocationStudyScreen(
//            contentPadding = contentPadding,
//            onRegisterScrollToTop = onRegisterScrollToTop,
//            onFilterClick = onFilterClick,
//            onItemClick = onItemClick
//        )
//    }
//}
//
//@Serializable
//data object PreferLocation : Route