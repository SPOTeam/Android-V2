package com.umcspot.spot.alert.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlertRequestDto(
    @SerialName("studyId")
    val studyId : Int
)
