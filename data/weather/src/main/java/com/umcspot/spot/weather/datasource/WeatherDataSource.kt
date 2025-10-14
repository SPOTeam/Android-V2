package com.umcspot.spot.weather.datasource

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.weather.dto.request.WeatherRequestDto
import com.umcspot.spot.weather.dto.response.WeatherResponseDto

interface WeatherDataSource {
    suspend fun getDummies(request: WeatherRequestDto): BaseResponse<WeatherResponseDto>
}