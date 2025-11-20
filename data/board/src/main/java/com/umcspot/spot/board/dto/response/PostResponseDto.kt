package com.umcspot.spot.board.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.BoardType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

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
    val id: Int,

    @SerialName("label")
    val label: BoardType,

    @SerialName("title")
    val title: String,

    @SerialName("content")
    val content : String,

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