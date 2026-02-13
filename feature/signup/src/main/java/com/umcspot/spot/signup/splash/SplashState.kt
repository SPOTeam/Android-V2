package com.umcspot.spot.signup.splash

data class LandingState(
    val successAutoLogin: Boolean = false,
)

sealed interface LandingSideEffect {
    data object NavigateToHome : LandingSideEffect
    data object NavigateToLanding : LandingSideEffect
    data class ShowSnackBar(val message: String) : LandingSideEffect
}