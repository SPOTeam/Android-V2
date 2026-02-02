package com.umcspot.spot.alert.datasourceimpl

import com.umcspot.spot.alert.datasource.AlertDataSource
import com.umcspot.spot.alert.dto.response.AlertResponseDto
import com.umcspot.spot.alert.dto.response.UnReadAlertResponseDto
import com.umcspot.spot.alert.service.AlertService
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import javax.inject.Inject

class AlertDataSourceImpl @Inject constructor(
    private val alertService: AlertService
) : AlertDataSource {
    override suspend fun getAlerts(
    ): BaseResponse<AlertResponseDto> =
        alertService.getAlerts()

    override suspend fun getUnReadAlerts(): BaseResponse<UnReadAlertResponseDto> =
        alertService.getUnReadAlerts()

    override suspend fun readAlert(notificationId: Long): NullResultResponse =
        alertService.readAlert(notificationId)
}