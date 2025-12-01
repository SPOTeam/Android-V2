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
    override suspend fun getWeather(request: Weather): Result<WeatherResult> =
        runCatching {
            val response = weatherService.getWeather(request.toData())
            response.result.toDomain()
        }.recoverCatching {
            // API 미연결/예외 시 더미로 복구
            WeatherResult.dummyFrom()
        }
}