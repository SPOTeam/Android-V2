package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoQueryResponseDto(
    @SerialName("pending")
    val pending: List<TodoItemDto>,
    @SerialName("completed")
    val completed: List<TodoItemDto>
)

@Serializable
data class TodoItemDto(
    @SerialName("id")
    val id: Long,
    @SerialName("content")
    val content: String,
    @SerialName("dueDate")
    val dueDate: String,
    @SerialName("isCompleted")
    val isCompleted: Boolean
)