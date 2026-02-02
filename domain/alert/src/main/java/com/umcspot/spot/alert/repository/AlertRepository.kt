package com.umcspot.spot.alert.repository

import com.umcspot.spot.alert.model.AlertResult

interface AlertRepository {
    suspend fun getAlerts(): Result<AlertResult>

    suspend fun getUnReadAlerts(): Result<Boolean>

    suspend fun readAlert(notificationId: Long): Result<Unit>
}