package com.umcspot.spot.home.model

import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.model.WeatherType
import com.umcspot.spot.study.model.ImageRef
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.weather.model.Weather
import com.umcspot.spot.weather.model.WeatherResult
import java.time.LocalTime

data class Home(
    val id: Int,
    val email: String,
    val weather: Weather
)

data class QuickMenuItem(
    val iconRes: Int,
    val label: String,
    val type: QuickMenuType
)