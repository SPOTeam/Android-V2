package com.umcspot.spot.alert.datasource

import com.umcspot.spot.alert.dto.response.AlertResponseDto
import com.umcspot.spot.network.model.BaseResponse

interface AlertDataSource {
    suspend fun getAlerts(): BaseResponse<AlertResponseDto>

}