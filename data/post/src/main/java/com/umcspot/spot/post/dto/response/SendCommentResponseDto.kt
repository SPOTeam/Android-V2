package com.umcspot.spot.post.dto.response

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class SendCommentResponseDto(

    @SerialName("commentId")
    val commentId : Long
)
