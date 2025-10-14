package com.umcspot.spot.weather.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.WeatherType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalTime

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class WeatherResponseDto(
    @SerialName("currentTime")
    val currentTime: String,

    @SerialName("weatherTemp")
    val weatherTemp: Double? = null,

    @SerialName("weatherType")
    val weatherType: WeatherType? = null,
)