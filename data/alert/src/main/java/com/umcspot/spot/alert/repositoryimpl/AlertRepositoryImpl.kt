package com.umcspot.spot.alert.repositoryimpl

import com.umcspot.spot.alert.mapper.toDomainList
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.alert.model.AppliedAlertResult
import com.umcspot.spot.alert.repository.AlertRepository
import com.umcspot.spot.alert.service.AlertService
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val studyService: AlertService
) : AlertRepository {
    override suspend fun getAlerts(): Result<AlertResult> =
        runCatching {
            val response = studyService.getAlerts()
            response.data.toDomainList()
        }.recoverCatching {
            getAlertDummies()
        }

    private fun getAlertDummies(): AlertResult =
        AlertResult(AlertResult.getAlertDummies())

    override suspend fun getAppliedAlerts(): Result<AppliedAlertResult> =
        runCatching {
            val response = studyService.getAppliedAlerts()
            response.data.toDomainList()
        }.recoverCatching {
            getAppliedAlertDummies()
        }

    private fun getAppliedAlertDummies(): AppliedAlertResult =
        AppliedAlertResult(AppliedAlertResult.getAppliedAlertDummies())

}