package com.example.core.data.home

import com.example.core.data.global.WeatherType
import com.example.core.data.study.StudyItem
import java.time.LocalTime

data class HomeUiState(
    val weatherTemp: Int? = null,
    val weatherType: WeatherType? = null,
    val currentTime: LocalTime? = null,
    val popularStudies: List<StudyItem> = emptyList(),
    val recommendedStudies: List<StudyItem> = emptyList(),
    val error: String? = null
)