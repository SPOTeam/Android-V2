package com.umcspot.spot.home.model

import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.weather.model.WeatherResult

data class HomeResult(
    val weatherInfo: WeatherResult,
    val popularStudies: List<StudyResult>,
    val recommendedStudies: List<StudyResult>
)