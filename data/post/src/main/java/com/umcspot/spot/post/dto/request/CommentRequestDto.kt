package com.umcspot.spot.post.dto.request

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CommentRequestDto(
    @SerialName("content")
    val content : String
)
