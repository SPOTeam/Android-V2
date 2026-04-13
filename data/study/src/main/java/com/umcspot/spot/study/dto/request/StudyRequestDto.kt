package com.umcspot.spot.study.dto.request

import com.umcspot.spot.model.StudyStyle
import com.umcspot.spot.model.StudyTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class StudyRequestDto(
    @SerialName("name") val name: String,
    @SerialName("maxMembers") val maxMembers: Int,
    @SerialName("hasFee") val hasFee: Boolean,
    @SerialName("amount") val amount: Int,
    @SerialName("description") val description: String,
    @SerialName("isOnline") val isOnline: Boolean,
    @SerialName("categories") val categories: List<StudyTheme>,
    @SerialName("styles") val styles: List<StudyStyle>,
    @SerialName("regionCodes") val regionCodes: List<String>
)