package com.umcspot.spot.alert.mapper

import com.umcspot.spot.alert.dto.response.AlertItem
import com.umcspot.spot.alert.dto.response.AlertResponseDto
import com.umcspot.spot.alert.dto.response.AppliedAlertItem
import com.umcspot.spot.alert.dto.response.AppliedAlertResponseDto
import com.umcspot.spot.alert.model.AlertInfo
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.alert.model.AppliedAlertInfo
import com.umcspot.spot.alert.model.AppliedAlertResult


fun AlertItem.toDomain() : AlertInfo =
    AlertInfo (
        id = this.id,
        kind = this.kind,
        title = this.title,
        studyImageRes = this.studyImageRes,
        subtitle = this.subtitle,
        isRead = this.isRead
    )

fun AlertResponseDto.toDomainList(): AlertResult =
    AlertResult(
        alerts = this.alerts.map(AlertItem::toDomain)
    )


fun AppliedAlertItem.toDomain() : AppliedAlertInfo =
    AppliedAlertInfo (
        id = this.id,
        title = this.title,
        studyImageRes = this.studyImageRes,
        subtitle = this.subtitle,
    )

fun AppliedAlertResponseDto.toDomainList(): AppliedAlertResult =
    AppliedAlertResult(
        alerts = this.alerts.map(AppliedAlertItem::toDomain)
    )
