package com.umcspot.spot.weather.model

import com.umcspot.spot.model.WeatherType
import java.time.LocalTime

data class WeatherResult (
    val currentTime: LocalTime,
    val weatherTemp: Double? = null,
    val weatherType: WeatherType? = null,
){
     companion object {
        fun dummyFrom(): WeatherResult =
            WeatherResult(
                currentTime = LocalTime.now(),
                weatherTemp = 23.4,
                weatherType = WeatherType.SUNNY
            )
    }
}