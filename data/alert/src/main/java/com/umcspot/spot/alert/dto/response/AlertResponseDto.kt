package com.umcspot.spot.alert.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.alert.model.ImageRef
import com.umcspot.spot.model.AlertKind
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AlertResponseDto(
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