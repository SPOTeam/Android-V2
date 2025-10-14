package com.umcspot.spot.weather.repositoryimpl

import com.umcspot.spot.weather.mapper.toData
import com.umcspot.spot.weather.mapper.toDomain
import com.umcspot.spot.weather.model.Weather
import com.umcspot.spot.weather.model.WeatherResult
import com.umcspot.spot.weather.repository.WeatherRepository
import com.umcspot.spot.weather.service.WeatherService
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRepository {
    override suspend fun getDummies(request : Weather): Result<WeatherResult> =
        runCatching {
            val response = weatherService.getDummies(request = request.toData())
            response.data.toDomain()
        }

}