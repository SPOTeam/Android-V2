package com.umcspot.spot.study.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoardCreateRequestDto(
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("isPrivate") val isPrivate: Boolean
)