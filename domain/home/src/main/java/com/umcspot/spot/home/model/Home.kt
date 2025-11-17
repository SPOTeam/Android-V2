package com.umcspot.spot.home.model

import com.umcspot.spot.model.QuickMenuType
import com.umcspot.spot.weather.model.Weather

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