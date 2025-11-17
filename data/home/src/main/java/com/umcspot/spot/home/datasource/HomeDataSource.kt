package com.umcspot.spot.home.datasource

import com.umcspot.spot.home.dto.request.HomeRequestDto
import com.umcspot.spot.home.dto.response.HomeResponseDto
import com.umcspot.spot.network.model.BaseResponse

interface HomeDataSource {
    suspend fun getHomeData(request: HomeRequestDto): BaseResponse<HomeResponseDto>
}