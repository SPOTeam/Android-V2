package com.umcspot.spot.study.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.ImageRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StudyResponseDto(
    @SerialName("content")
    val content : List<Study>,
    @SerialName("hasNext")
    val hasNext : Boolean,
    @SerialName("nextCursor")
    val nextCursor : String? = null,
    @SerialName("totalElements")
    val totalElements : Int,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Study (
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String,

    @SerialName("maxMembers")
    val maxMembers : Int,

    @SerialName("currentMembers")
    val currentMembers: Int,

    @SerialName("likeCount")
    val likeCount: Int,

    @SerialName("isLiked")
    val isLiked: Boolean,

    @SerialName("isOwner")
    val isOwner: Boolean,

    @SerialName("isAlone")
    val isAlone: Boolean,

    @SerialName("hitCount")
    val hitCount: Int = 0,

    @SerialName("profileImageUrl")
    val profileImageUrl: String?
)