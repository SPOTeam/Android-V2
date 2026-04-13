package com.umcspot.spot.study.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyPostCommentRequestDto(
    @SerialName("content")
    val content: String
)
