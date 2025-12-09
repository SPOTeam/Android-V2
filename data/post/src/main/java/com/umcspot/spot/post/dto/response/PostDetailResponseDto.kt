package com.umcspot.spot.post.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.PostType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PostDetailResponseDto(
    @SerialName("postId")
    val postId : Long,

    @SerialName("title")
    val title : String,

    @SerialName("content")
    val content : String,

    @SerialName("imageUrl")
    val imageUrl : String?,

    @SerialName("postType")
    val postType : PostType,

    @SerialName("isLiked")
    val isLiked : Boolean,

    @SerialName("writer")
    val writer : Writer,

    @SerialName("stats")
    val stats : Stats,

    @SerialName("createdAt")
    val createdAt : String,

    @SerialName("comments")
    val comments : List<CommentResponse>,

    @SerialName("commentCount")
    val commentCount : Long,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Writer (
    @SerialName("writerId")
    val writerId : Long,

    @SerialName("nickname")
    val nickname : String,

    @SerialName("profileImageUrl")
    val profileImageUrl : String?
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

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CommentResponse (
    @SerialName("commentId")
    val commentId : Long,

    @SerialName("content")
    val content : String,

    @SerialName("writer")
    val writer : Writer,

    @SerialName("createdAt")
    val createdAt : String
)