package com.umcspot.spot.alert.mapper

import com.umcspot.spot.alert.dto.response.AlertItem
import com.umcspot.spot.alert.dto.response.AlertResponseDto
import com.umcspot.spot.alert.dto.response.UnReadAlertResponseDto
import com.umcspot.spot.alert.model.AlertInfo
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.model.formatCreatedAt
import com.umcspot.spot.model.toImageRef


fun AlertItem.toDomain() : AlertInfo =
    AlertInfo (
        notificationId = this.notificationId.toLong(),
        type = this.type,
        title = this.title,
        body = this.body,
        imageUrl = this.imageUrl.toImageRef(),
        referenceType = this.referenceType,
        referenceId = this.referenceId.toLong(),
        isRead = this.isRead,
        createdAt = this.createdAt.formatCreatedAt()
    )

fun AlertResponseDto.toDomainList(): AlertResult =
    AlertResult(
        notifications = this.notifications.map(AlertItem::toDomain),
        totalCount = this.totalCount
    )

fun UnReadAlertResponseDto.toDomain() : Boolean = this.hasUnreadNotifications
