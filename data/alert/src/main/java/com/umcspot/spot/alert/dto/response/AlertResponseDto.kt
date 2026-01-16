package com.umcspot.spot.alert.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.ImageRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AlertResponseDto(
    @SerialName("studies")
    val studies : List<AlertItem>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AlertItem(
    @SerialName("applicationId")
    val applicationId: String,

    @SerialName("studyId")
    val studyId: String,

    @SerialName("title")
    val title: String,

    @SerialName("profileImageRef")
    val profileImageRef: ImageRef,
)