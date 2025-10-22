package com.umcspot.spot.alert.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.AlertKind
import com.umcspot.spot.model.ImageRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AlertResponseDto(
    @SerialName("alerts")
    val alerts : List<AlertItem>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AlertItem(
    @SerialName("id")
    val id: Int,

    @SerialName("kind")
    val kind: AlertKind,

    @SerialName("title")
    val title: String,

    @SerialName("subtitle")
    val subtitle: String,

    @SerialName("studyImageRes")
    val studyImageRes: ImageRef,

    @SerialName("isRead")
    val isRead: Boolean = false
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AppliedAlertResponseDto(
    @SerialName("alerts")
    val alerts : List<AppliedAlertItem>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AppliedAlertItem(
    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String,

    @SerialName("subtitle")
    val subtitle: String,

    @SerialName("studyImageRes")
    val studyImageRes: ImageRef,
)