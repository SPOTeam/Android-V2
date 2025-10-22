package com.umcspot.spot.alert.service

import com.umcspot.spot.alert.dto.response.AlertResponseDto
import com.umcspot.spot.alert.dto.response.AppliedAlertResponseDto
import com.umcspot.spot.network.model.BaseResponse
import retrofit2.http.GET

interface AlertService {
    @GET("/api/v1/service")
    suspend fun getAlerts(

    ): BaseResponse<AlertResponseDto>

    @GET("/api/v1/service")
    suspend fun getAppliedAlerts(

    ): BaseResponse<AppliedAlertResponseDto>
}