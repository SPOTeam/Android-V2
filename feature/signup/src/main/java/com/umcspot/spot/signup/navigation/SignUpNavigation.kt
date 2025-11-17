package com.umcspot.spot.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.signup.SignUpScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) {
    navigate(SignUp, navOptions)
}

fun NavGraphBuilder.signupGraph() {
    composable<SignUp> {
        SignUpScreen(

        )
    }
}

@Serializable
data object SignUp : Route