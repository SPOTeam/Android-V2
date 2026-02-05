package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoCreateResponseDto(
    @SerialName("todoId")
    val todoId: Long
)