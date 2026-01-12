package com.umcspot.spot.weather.datasource

import com.umcspot.spot.weather.dto.response.WeatherResponseDto

interface WeatherDataSource {
    suspend fun getWeather(serviceKey : String, dataType : String, baseDate : String, baseTime : String, nx : Int, ny : Int): WeatherResponseDto
}