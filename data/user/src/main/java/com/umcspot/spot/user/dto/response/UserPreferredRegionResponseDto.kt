package com.umcspot.spot.user.dto.response

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UserPreferredRegionResponseDto(
    @SerialName("regionCodes")
    val regionCodes: List<String>,

    @SerialName("totalCount")
    val totalCount: Int
)