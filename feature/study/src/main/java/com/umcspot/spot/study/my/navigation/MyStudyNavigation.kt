package com.umcspot.spot.study.my.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.MainTabRoute
import com.umcspot.spot.study.my.MyStudyRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMyStudy(navOptions: NavOptions? = null) {
    navigate(MyStudy, navOptions)
}

fun NavGraphBuilder.myStudyGraph(
    contentPadding: PaddingValues,
    navigateToStudyDetail: (Long) -> Unit,
) {
    composable<MyStudy> {
        MyStudyRoute(
            contentPadding = contentPadding,
            navigateToStudyDetail = navigateToStudyDetail
        )
    }
}

@Serializable
data object MyStudy : MainTabRoute