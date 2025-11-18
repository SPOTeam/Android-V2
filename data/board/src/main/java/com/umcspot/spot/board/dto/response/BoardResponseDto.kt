package com.umcspot.spot.board.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.BoardType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class LabeledBoardResponseDto(
    @SerialName("boardItems")
    val boardItems : List<LabeledBoardItem>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class LabeledBoardItem (
    @SerialName("id")
    val id : Int,

    @SerialName("label")
    val label : BoardType,

    @SerialName("title")
    val title : String,

    @SerialName("content")
    val content : String,

    @SerialName("count")
    val count : Int
)