package com.umcspot.spot.weather.repository

import com.umcspot.spot.weather.model.Weather
import com.umcspot.spot.weather.model.WeatherResult

interface WeatherRepository {
    suspend fun getDummies(request : Weather): Result<WeatherResult>
}