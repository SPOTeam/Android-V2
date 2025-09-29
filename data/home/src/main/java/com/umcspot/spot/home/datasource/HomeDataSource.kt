package com.umcspot.spot.home.datasource

import com.umcspot.spot.home.dto.request.HomeRequestDto
import com.umcspot.spot.home.dto.response.HomeResponseDto
import network.model.BaseResponse

interface HomeDataSource {
    suspend fun getDummies(request: HomeRequestDto): BaseResponse<HomeResponseDto>
}