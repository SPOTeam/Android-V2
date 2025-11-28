package com.umcspot.spot.study.register.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.umcspot.spot.navigation.Route
import com.umcspot.spot.study.register.RegisterStudyRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToRegisterStudy(navOptions: NavOptions? = null) {
    
    navigate(RegisterStudy, navOptions)
}


fun NavGraphBuilder.registerStudyGraph(
    contentPadding : PaddingValues,
    onBackClick: () -> Unit,
//    navigateToNextScreen: (Long) -> Unit
    navigateToHome: () -> Unit
) {
    composable<RegisterStudy> {
        
        RegisterStudyRoute(
            contentPadding = contentPadding,
            onBackClick = onBackClick,
//            navigateToNext = { createdStudyId ->
//                navigateToNextScreen(createdStudyId)
//            }
            navigateToHome = navigateToHome
        )
    }
}

@Serializable
data object RegisterStudy : Route
