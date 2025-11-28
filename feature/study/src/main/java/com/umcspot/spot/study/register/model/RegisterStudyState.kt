package com.umcspot.spot.study.register.model

import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.StudyTheme

data class RegisterStudyState(

    val studyName: String = "",
    val studyThemes: List<StudyTheme> = emptyList(),

    val activityType: ActivityType? = null,
    val isSheetVisible: Boolean = false,
    val locationQuery: String = "",
    val locationResults: List<LocationRow> = emptyList(),
    val selectedRegions: List<String> = emptyList(),

    val memberCount: Int = 2,
    val hasFee: Boolean? = null,
    val feeAmount: String = "",

    val networkingPreference: Int? = null,
    val goalDurationPreference: Int? = null,
    val discussionPreference: Int? = null,
    val learningPreference: Int? = null,
    val flexibilityPreference: Int? = null,

    val description: String = "",
    val studyImageUri: String? = null
)

sealed interface RegisterStudySideEffect {
    data object NavigateToHome : RegisterStudySideEffect
    data class ShowSnackBar(val message: String) : RegisterStudySideEffect
}