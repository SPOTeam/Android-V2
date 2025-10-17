package com.umcspot.spot.home.model

import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.weather.model.WeatherResult

data class HomeResult(
    val weatherInfo: WeatherResult,
    val popularStudies: StudyResultList,
    val recommendedStudies: StudyResultList
)