package com.umcspot.spot.study.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.ImageRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StudyPostsResponseDto(
    @SerialName("posts")
    val posts : List<StudyPost>,
    @SerialName("hasNext")
    val hasNext : Boolean,
    @SerialName("nextCursor")
    val nextCursor : String? = null,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StudyPost (
    @SerialName("postId")
    val postId: String,

    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content: String,

    @SerialName("isPinned")
    val isPinned : Boolean,

    @SerialName("isLiked")
    val isLiked: Boolean,

    @SerialName("stats")
    val stats : Stats,

    @SerialName("createdAt")
    val createdAt: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Stats (
    @SerialName("likeCount")
    val likeCount: Int,

    @SerialName("viewCount")
    val viewCount: Int,

    @SerialName("commentCount")
    val commentCount: Int,
)