package com.umcspot.spot.home.service


import com.umcspot.spot.home.dto.request.HomeRequestDto
import com.umcspot.spot.home.dto.response.HomeResponseDto
import com.umcspot.spot.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface HomeService {
    @POST("/api/v1/service")
    suspend fun getDummies(
        @Body request: HomeRequestDto
    ): BaseResponse<HomeResponseDto>
}