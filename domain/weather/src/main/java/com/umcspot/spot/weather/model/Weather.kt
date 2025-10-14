package com.umcspot.spot.weather.model

import com.umcspot.spot.model.WeatherType
import java.time.LocalTime

data class Weather(
    var longitude : Double, // 경도
    var latitude : Double   // 위도
)