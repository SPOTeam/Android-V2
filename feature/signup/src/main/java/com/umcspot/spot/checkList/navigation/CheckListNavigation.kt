package com.umcspot.spot.checkList.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.checkList.CheckListScreen
import com.umcspot.spot.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToCheckList(navOptions: NavOptions? = null) {
    navigate(CheckList, navOptions)
}

fun NavGraphBuilder.checkListGraph(
    contentPadding : PaddingValues,
    onNextClick: () -> Unit,
) {
    composable<CheckList> {
        CheckListScreen(
            contentPadding = contentPadding,
            onNextClick = onNextClick
        )
    }
}

@Serializable
data object CheckList : Route