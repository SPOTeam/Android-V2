package com.umcspot.spot.checkList.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.checkList.CheckListScreen
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.signup.SignUpViewModel
import kotlinx.serialization.Serializable

fun NavController.navigateToCheckList(navOptions: NavOptions? = null) {
    navigate(CheckList, navOptions)
}

fun NavGraphBuilder.checkListGraph(
    navController: NavHostController,
    contentPadding : PaddingValues,
    onNextClick: () -> Unit,
) {
    composable<CheckList> { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(navController.graph.id)
        }
        val signUpViewModel: SignUpViewModel = hiltViewModel(parentEntry)

        CheckListScreen(
            contentPadding = contentPadding,
            signUpViewModel= signUpViewModel,
            onNextClick = onNextClick
        )
    }
}

@Serializable
data object CheckList : Route