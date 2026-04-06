package com.umcspot.spot.study.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyApplyRequestDto(
    @SerialName("message")
    val message: String
)