package com.umcspot.spot.board.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.PostType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PostResponseDto(
    @SerialName("postItems")
    val postItems : List<PostItem>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PostItem (
    @SerialName("id")
    val postId: Long,

    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content : String,

    @SerialName("label")
    val label: PostType,

    @SerialName("likeNum")
    val likeNum: Int,

    @SerialName("commentNum")
    val commentNum: Int,

    @SerialName("viewNum")
    val viewNum: Int,

    @SerialName("date")
    val date: String,

    @SerialName("time")
    val time : String
)