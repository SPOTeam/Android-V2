package com.umcspot.spot.home.datasourceimpl

import com.umcspot.spot.home.datasource.HomeDataSource
import com.umcspot.spot.home.dto.request.HomeRequestDto
import com.umcspot.spot.home.dto.response.HomeResponseDto
import com.umcspot.spot.home.service.HomeService
import com.umcspot.spot.network.model.BaseResponse
import javax.inject.Inject


class HomeDataSourceImpl @Inject constructor(
    private val homeService: HomeService
) : HomeDataSource {
    override suspend fun getHomeData(
        request: HomeRequestDto
    ): BaseResponse<HomeResponseDto> =
        homeService.getHomeData(request)
}