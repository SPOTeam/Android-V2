package com.umcspot.spot.board.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.PostType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PostListResponseDto(
    @SerialName("posts")
    val posts : List<PostItem>,
    @SerialName("hasNext")
    val hasNext : Boolean,
    @SerialName("nextCursor")
    val nextCursor : Long,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PostItem (
    @SerialName("postId")
    val postId: Long,

    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content : String,

    @SerialName("postType")
    val postType : PostType,

    @SerialName("stats")
    val stats : Stats,

    @SerialName("createdAt")
    val createdAt : String,

    @SerialName("isLiked")
    val isLiked : Boolean,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Stats (
    @SerialName("likeCount")
    val likeCount : Long,
    @SerialName("viewCount")
    val viewCount : Long,
    @SerialName("commentCount")
    val commentCount : Long,
)