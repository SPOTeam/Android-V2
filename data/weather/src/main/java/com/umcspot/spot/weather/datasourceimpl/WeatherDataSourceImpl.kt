package com.umcspot.spot.weather.datasourceimpl

import com.umcspot.spot.weather.datasource.WeatherDataSource
import com.umcspot.spot.weather.dto.response.WeatherResponseDto
import com.umcspot.spot.weather.service.WeatherService
import javax.inject.Inject


class WeatherDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherDataSource {

    override suspend fun getWeather(
        serviceKey : String,
        dataType: String,
        baseDate : String,
        baseTime : String,
        nx : Int,
        ny : Int
    ): WeatherResponseDto =
        weatherService.getWeather(
            serviceKey = serviceKey,
            dataType = dataType,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny
        )
}