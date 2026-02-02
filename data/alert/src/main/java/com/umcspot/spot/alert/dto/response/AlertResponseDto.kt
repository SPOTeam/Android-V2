package com.umcspot.spot.alert.dto.response

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AlertResponseDto(
    @SerialName("notifications")
    val notifications : List<AlertItem>,

    @SerialName("totalCount")
    val totalCount: Int
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AlertItem(
    @SerialName("notificationId")
    val notificationId: String,

    @SerialName("type")
    val type: String,

    @SerialName("title")
    val title: String,

    @SerialName("body")
    val body: String,

    @SerialName("imageUrl")
    val imageUrl: String?,

    @SerialName("referenceType")
    val referenceType: String,

    @SerialName("referenceId")
    val referenceId: String,

    @SerialName("isRead")
    val isRead: Boolean,

    @SerialName("createdAt")
    val createdAt: String,
)