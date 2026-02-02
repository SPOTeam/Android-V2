package com.umcspot.spot.alert.model

import com.umcspot.spot.model.ImageRef

/******** 일반 알람 ********/
data class AlertResult (
    val notifications : List<AlertInfo>,
    val totalCount : Int
)

data class AlertInfo(
    val notificationId: Long,
    val type: String,
    val title: String,
    val body: String,
    val imageUrl: ImageRef,
    val referenceType: String,
    val referenceId: Long,
    val isRead: Boolean,
    val createdAt: String,
)