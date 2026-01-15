package com.umcspot.spot.post.dto.request

import android.annotation.SuppressLint
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ReportPostRequestDto(
    @SerialName("reason")
    val reason : String
)