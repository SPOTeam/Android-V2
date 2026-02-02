package com.umcspot.spot.alert.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.ImageRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UnReadAlertResponseDto(
    @SerialName("hasUnreadNotifications")
    val hasUnreadNotifications : Boolean
)
