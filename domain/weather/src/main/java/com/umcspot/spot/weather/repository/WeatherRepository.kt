package com.umcspot.spot.weather.repository

import com.umcspot.spot.weather.model.WeatherResult

interface WeatherRepository {
    suspend fun getWeather(longitude : Long, latitude : Long): Result<WeatherResult>
}