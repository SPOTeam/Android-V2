package com.umcspot.spot.weather.service


import com.umcspot.spot.weather.dto.response.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("1360000/VilageFcstInfoService_2.0/getUltraSrtNcst")
    suspend fun getWeather(
        @Query(value = "ServiceKey", encoded = true) serviceKey: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 1000,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String, // 예: 20210628
        @Query("base_time") baseTime: String, // 예: 0600
        @Query("nx") nx: Int,
        @Query("ny") ny: Int,
    ): WeatherResponseDto
}