package com.umcspot.spot.user.dto.request

import com.umcspot.spot.model.StudyTheme
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserThemeRequestDto(
    @SerialName("categories")
    val categories: List<StudyTheme>
)

@Serializable
data class UserNameRequestDto(
    @SerialName("name")
    val name: String
)