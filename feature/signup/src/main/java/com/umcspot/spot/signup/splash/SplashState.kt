package com.umcspot.spot.signup.splash

data class SplashState(
    val successAutoLogin: Boolean = false,
)

sealed interface SplashSideEffect {
    data object NavigateToHome : SplashSideEffect
    data object NavigateToLanding : SplashSideEffect
    data class ShowSnackBar(val message: String) : SplashSideEffect
}
