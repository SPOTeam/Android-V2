package com.umcspot.spot.weather.repositoryimpl

import com.umcspot.spot.weather.mapper.toDomain
import com.umcspot.spot.weather.model.WeatherResult
import com.umcspot.spot.weather.repository.WeatherRepository
import com.umcspot.spot.weather.service.WeatherService
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRepository {
    override suspend fun getWeather(longitude : Long, latitude : Long): Result<WeatherResult> =
        runCatching {
            val response = weatherService.getWeather(longitude, latitude)
            response.result.toDomain()
        }.recoverCatching {
            // API 미연결/예외 시 더미로 복구
            WeatherResult.dummyFrom()
        }
}