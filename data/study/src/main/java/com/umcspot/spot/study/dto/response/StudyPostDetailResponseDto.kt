package com.umcspot.spot.study.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.ImageRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StudyPostDetailResponseDto (
    @SerialName("postId")
    val postId: String,

    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content: String,

    @SerialName("isPinned")
    val isPinned : Boolean,

    @SerialName("isOwner")
    val isOwner: Boolean,

    @SerialName("isLiked")
    val isLiked: Boolean,

    @SerialName("writer")
    val writer : Writer?,

    @SerialName("stats")
    val stats : Stats,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("comments")
    val comments : List<Comment>
)

@Serializable
data class Writer(
    @SerialName("writerId")
    val writerId: Long,

    @SerialName("nickname")
    val nickname: String,

    @SerialName("profileImageUrl")
    val profileImageUrl: String?
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Comment (
    @SerialName("commentId")
    val commentId : String,

    @SerialName("content")
    val content : String,

    @SerialName("isOwner")
    val isOwner : Boolean,

    @SerialName("writer")
    val writer : Writer?,

    @SerialName("createdAt")
    val createdAt : String
)