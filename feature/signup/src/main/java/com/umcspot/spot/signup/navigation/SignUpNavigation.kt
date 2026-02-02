package com.umcspot.spot.signup.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.signup.checkList.CheckListRoute
import com.umcspot.spot.signup.landing.LandingRoute
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.signup.SignUpRoute
import com.umcspot.spot.signup.saving.SavingRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) {
    navigate(SignUp, navOptions)
}

fun NavController.navigateToCheckList(navOptions: NavOptions? = null) {
    navigate(CheckList, navOptions)
}

fun NavController.navigateToSaving(navOptions: NavOptions? = null) {
    navigate(Saving, navOptions)
}

fun NavController.navigateToLanding(navOptions: NavOptions? = null) {
    navigate(Landing, navOptions)
}

fun NavGraphBuilder.signupGraph(
    navigateToSignUp: () -> Unit,
    navigateToCheckList: () -> Unit,
    navigateToSaving: () -> Unit,
    navigateToHome: () -> Unit,
    contentPadding: PaddingValues,
) {
    composable<Landing> {
        LandingRoute(
            navigateToSignUp = navigateToSignUp,
            navigateToHome = navigateToHome
        )
    }
    composable<SignUp> {
        SignUpRoute(
            navigateToCheckList = navigateToCheckList,
            contentPadding = contentPadding,
        )
    }
    composable<CheckList> { backStackEntry ->
        CheckListRoute(
            navigateToSaving = navigateToSaving,
            contentPadding = contentPadding,
        )
    }
    composable<Saving> {
        SavingRoute(
            contentPadding = contentPadding,
            navigateToHome = navigateToHome
        )
    }
}

@Serializable
data object Landing : Route

@Serializable
data object SignUp : Route

@Serializable
data object CheckList : Route

@Serializable
data object Saving : Route
