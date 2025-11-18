package com.umcspot.spot.signup.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.signup.SignUpScreen
import com.umcspot.spot.signup.SignUpViewModel
import kotlinx.serialization.Serializable

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) {
    navigate(SignUp, navOptions)
}

fun NavGraphBuilder.signupGraph(
    navController: NavHostController,
    contentPadding : PaddingValues,
    onNextClick : () -> Unit
) {
    composable<SignUp> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            // 🔹 NavHost 루트 그래프 기준으로 ViewModel 스코프
            navController.getBackStackEntry(navController.graph.id)
        }
        val signUpViewModel: SignUpViewModel = hiltViewModel(parentEntry)

        SignUpScreen(
            contentPadding = contentPadding,
            viewmodel = signUpViewModel,
            onNextClick = onNextClick
        )
    }
}

@Serializable
data object SignUp : Route