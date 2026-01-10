package com.umcspot.spot.weather.datasource

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.weather.dto.response.WeatherResponseDto

interface WeatherDataSource {
    suspend fun getWeather(longitude : Long, latitude : Long): BaseResponse<WeatherResponseDto>
}