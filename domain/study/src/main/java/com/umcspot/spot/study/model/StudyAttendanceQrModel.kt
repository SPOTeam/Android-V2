package com.umcspot.spot.study.model

data class StudyAttendanceQrModel(
    val attendanceActive: Boolean,
    val qrCodeImageUrl: String?
)