package com.umcspot.spot.alert.datasource

import com.umcspot.spot.alert.dto.response.AlertResponseDto
import com.umcspot.spot.alert.dto.response.UnReadAlertResponseDto
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse

interface AlertDataSource {
    suspend fun getAlerts(): BaseResponse<AlertResponseDto>

    suspend fun getUnReadAlerts(): BaseResponse<UnReadAlertResponseDto>

    suspend fun readAlert(notificationId : Long): NullResultResponse

}