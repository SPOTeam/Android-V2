package com.umcspot.spot.signup.landing

data class LandingState(
    val isLoading: Boolean = false,
)

sealed interface LandingSideEffect {
    data object NavigateToSignUp : LandingSideEffect
    data object NavigateToHome : LandingSideEffect
    data class ShowSnackBar(val message: String) : LandingSideEffect
}