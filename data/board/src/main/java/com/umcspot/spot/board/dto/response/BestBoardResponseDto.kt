package com.umcspot.spot.board.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.PostType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class BestBoardResponseDto(
    @SerialName("hotPosts")
    val hotPosts : List<BestBoardItem>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class BestBoardItem (
    @SerialName("postId")
    val postId : Long,

    @SerialName("title")
    val title : String,

    @SerialName("content")
    val content : String,

    @SerialName("commentCount")
    val commentCount : Int,

    @SerialName("postType")
    val postType : PostType,
)