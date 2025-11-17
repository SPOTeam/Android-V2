package com.umcspot.spot.weather.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherRequestDto(
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("latitude")
    val latitude: Double
)
