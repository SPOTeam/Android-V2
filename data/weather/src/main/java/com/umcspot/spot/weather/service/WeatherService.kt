package com.umcspot.spot.weather.service


import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.weather.dto.request.WeatherRequestDto
import com.umcspot.spot.weather.dto.response.WeatherResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("/api/v1/service")
    suspend fun getWeather(
        @Query("longitude") longitude : Long,
        @Query("latitude") latitude : Long,
    ): BaseResponse<WeatherResponseDto>
}