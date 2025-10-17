package com.umcspot.spot.weather.service


import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.weather.dto.request.WeatherRequestDto
import com.umcspot.spot.weather.dto.response.WeatherResponseDto
import retrofit2.http.Body
import retrofit2.http.GET


interface WeatherService {
    @GET("/api/v1/service")
    suspend fun getWeather(
        @Body request: WeatherRequestDto
    ): BaseResponse<WeatherResponseDto>
}