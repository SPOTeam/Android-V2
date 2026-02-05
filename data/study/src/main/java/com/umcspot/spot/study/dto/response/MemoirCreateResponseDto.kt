package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemoirCreateResponseDto(
    @SerialName("reviewId") val reviewId: Long
)