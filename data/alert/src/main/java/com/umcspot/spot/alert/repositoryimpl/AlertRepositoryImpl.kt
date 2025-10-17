package com.umcspot.spot.alert.repositoryimpl

import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.alert.repository.AlertRepository
import com.umcspot.spot.alert.service.AlertService
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val studyService: AlertService
) : AlertRepository {
    override suspend fun getAlerts(): Result<AlertResult> =
        runCatching {
            val response = studyService.getAlerts()
            response.data.toDomain()
        }
}