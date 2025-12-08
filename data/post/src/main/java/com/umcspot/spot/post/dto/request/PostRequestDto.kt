package com.umcspot.spot.post.dto.request

import android.annotation.SuppressLint
import com.umcspot.spot.model.SortType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PostDetailRequestDto(
    @SerialName("postId")
    val postId : Long
)
