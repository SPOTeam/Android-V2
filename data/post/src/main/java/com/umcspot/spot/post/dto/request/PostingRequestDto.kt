package com.umcspot.spot.post.dto.request

import android.annotation.SuppressLint
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PostingRequestDto (

    @SerialName("request")
    val request : RequestPosting,

    @SerialName("imageFile")
    val imageFile: ImageRef
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class RequestPosting(
    @SerialName("title")
    val title : String,

    @SerialName("content")
    val content : String,

    @SerialName("postType")
    val postType: PostType,
)