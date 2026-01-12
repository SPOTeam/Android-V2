package com.umcspot.spot.weather.repository

import com.umcspot.spot.weather.model.WeatherResult

interface WeatherRepository {
    suspend fun getWeather(longitude : Double, latitude : Double): Result<WeatherResult>
}