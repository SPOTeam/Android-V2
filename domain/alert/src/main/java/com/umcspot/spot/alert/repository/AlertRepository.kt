package com.umcspot.spot.alert.repository

import com.umcspot.spot.alert.model.Alert
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.alert.model.AppliedAlertResult

interface AlertRepository {
    suspend fun getAlerts(): Result<AlertResult>
    suspend fun getAppliedAlerts():Result<AppliedAlertResult>
}