package com.umcspot.spot.study.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.study.MyStudyScreen
import com.umcspot.spot.navigation.MainTabRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMyStudy(navOptions: NavOptions? = null) {
    navigate(MyStudy, navOptions)
}

fun NavGraphBuilder.myStudyGraph() {
    composable<MyStudy> {
        MyStudyScreen()
    }
}

@Serializable
data object MyStudy : MainTabRoute