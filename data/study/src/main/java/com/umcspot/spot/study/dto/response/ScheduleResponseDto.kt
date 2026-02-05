package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponseDto(
    @SerialName("scheduleId") val scheduleId: Long,
    @SerialName("title") val title: String,
    @SerialName("startAt") val startAt: String,
    @SerialName("endAt") val endAt: String,
    @SerialName("isNow") val isNow: Boolean
)