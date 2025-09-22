package com.umcspot.spot.home.datasourceimpl

import com.umcspot.spot.home.datasource.HomeDataSource
import com.umcspot.spot.home.dto.request.HomeRequestDto
import com.umcspot.spot.home.dto.response.HomeResponseDto
import com.umcspot.spot.home.service.HomeService
import network.model.BaseResponse
import javax.inject.Inject


class HomeDataSourceImpl @Inject constructor(
    private val homeService: HomeService
) : HomeDataSource {
    override suspend fun getDummies(
        request: HomeRequestDto
    ): BaseResponse<HomeResponseDto> =
        homeService.getDummies(request)
}