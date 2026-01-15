package com.umcspot.spot.alert.repositoryimpl

import android.util.Log
import com.umcspot.spot.alert.datasource.AlertDataSource
import com.umcspot.spot.alert.mapper.toDomainList
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.alert.repository.AlertRepository
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val alertDataSource: AlertDataSource
) : AlertRepository {
    override suspend fun getAlerts(): Result<AlertResult> =
        runCatching {
            val response = alertDataSource.getAlerts()
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("AlertRepositoryImpl", "getAlerts error", e)
        }

}