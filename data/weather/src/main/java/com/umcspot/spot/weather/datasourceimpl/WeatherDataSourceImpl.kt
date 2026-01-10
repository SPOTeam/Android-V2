package com.umcspot.spot.weather.datasourceimpl

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.weather.datasource.WeatherDataSource
import com.umcspot.spot.weather.dto.response.WeatherResponseDto
import com.umcspot.spot.weather.service.WeatherService
import javax.inject.Inject


class WeatherDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherDataSource {

    override suspend fun getWeather(
        longitude : Long,
        latitude : Long
    ): BaseResponse<WeatherResponseDto> =
        weatherService.getWeather(longitude, latitude)
}