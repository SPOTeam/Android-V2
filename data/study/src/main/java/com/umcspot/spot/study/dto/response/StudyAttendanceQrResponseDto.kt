package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyAttendanceQrResponseDto(
    @SerialName("attendanceActive")
    val attendanceActive: Boolean,
    @SerialName("qrCodeImageUrl")
    val qrCodeImageUrl: String?
)