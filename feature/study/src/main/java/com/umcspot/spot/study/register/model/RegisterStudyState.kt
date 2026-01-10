package com.umcspot.spot.study.register.model

import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.StudyStyle
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.model.StudyPersonality

data class RegisterStudyState(
    val studyName: String = "",
    val studyThemes: List<StudyTheme> = emptyList(),

    val activityType: ActivityType? = null,
    val isSheetVisible: Boolean = false,
    val locationQuery: String = "",
    val locationResults: List<LocationRow> = emptyList(),
    val selectedRegions: List<LocationRow> = emptyList(),

    val memberCount: Int = 2,
    val hasFee: Boolean? = null,
    val feeAmount: String = "",

    val personalitySelections: Map<StudyPersonality, StudyStyle> = emptyMap(),

    val description: String = "",
    val studyImageUri: String? = null,

    val isSuccessModalVisible: Boolean = false,
    val createdStudyId: Long? = null
)

sealed interface RegisterStudySideEffect {
    data class ShowSnackBar(val message: String) : RegisterStudySideEffect
}