package com.umcspot.spot.alert.repository

import com.umcspot.spot.alert.model.AlertResult

interface AlertRepository {
    suspend fun getAlerts(): Result<AlertResult>
}