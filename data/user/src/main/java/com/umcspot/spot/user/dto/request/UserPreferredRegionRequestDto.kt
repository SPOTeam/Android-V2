package com.umcspot.spot.user.dto.request

import com.umcspot.spot.model.StudyTheme
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferredRegionRequestDto(
    @SerialName("regionCodes")
    val regionCodes: List<String>
)