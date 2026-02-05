package com.umcspot.spot.study.model

import java.time.LocalDateTime

data class StudyScheduleModel(
    val id: Long,
    val title: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val isNow: Boolean
)