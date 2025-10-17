package com.umcspot.spot.weather.repository

import com.umcspot.spot.weather.model.Weather
import com.umcspot.spot.weather.model.WeatherResult

interface WeatherRepository {
    suspend fun getWeather(request : Weather): Result<WeatherResult>
}