package com.umcspot.spot.signup

data class SignupState(
    val originalName: String? = null,
    val currentName: String? = null,
    val isPrivacyChecked: Boolean = false,
    val isUniqueChecked: Boolean = false
)

sealed interface SignupSideEffect {
    data object NavigateToCheckList : SignupSideEffect
    data class ShowSnackBar(val message: String) : SignupSideEffect
}