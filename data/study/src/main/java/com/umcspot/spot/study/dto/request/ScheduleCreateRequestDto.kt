package com.umcspot.spot.study.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleCreateRequestDto(
    @SerialName("title")
    val title: String,
    @SerialName("locationInfo")
    val locationInfo: String,
    @SerialName("startAt")
    val startAt: String,
    @SerialName("endAt")
    val endAt: String
)