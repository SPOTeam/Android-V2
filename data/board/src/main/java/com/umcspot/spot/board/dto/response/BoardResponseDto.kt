package com.umcspot.spot.board.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.BoardType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class RankedBoardResponseDto(
    @SerialName("boardItems")
    val boardItems : List<RankedBoardItem>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class RankedBoardItem (
    @SerialName("id")
    val id : Int,

    @SerialName("title")
    val title : String,

    @SerialName("count")
    val count : Int
)

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

    @SerialName("count")
    val count : Int
)