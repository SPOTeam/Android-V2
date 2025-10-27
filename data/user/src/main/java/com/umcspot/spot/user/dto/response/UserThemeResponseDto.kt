package com.umcspot.spot.user.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.StudyTheme
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UserThemeResponseDto (
    @SerialName("userThemes")
    val userThemes : List<StudyTheme>
)