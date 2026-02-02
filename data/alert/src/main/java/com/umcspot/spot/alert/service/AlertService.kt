package com.umcspot.spot.alert.service

import com.umcspot.spot.alert.dto.response.AlertResponseDto
import com.umcspot.spot.alert.dto.response.UnReadAlertResponseDto
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AlertService {
    @GET("/api/notifications/me")
    suspend fun getAlerts(
    ): BaseResponse<AlertResponseDto>

    @GET("/api/notifications/me/unread")
    suspend fun getUnReadAlerts(
    ): BaseResponse<UnReadAlertResponseDto>

    @GET("/api/notifications/{notificationId}/read")
    suspend fun readAlert(
        @Path("notificationId") notificationId: Long,
    ): NullResultResponse
}