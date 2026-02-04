package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyMonthlyScheduleResponseDto(
    @SerialName("schedules") val schedules: List<ScheduleResponseDto>,
    @SerialName("totalCount") val totalCount: Int
)