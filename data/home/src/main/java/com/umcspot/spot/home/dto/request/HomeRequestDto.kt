package com.umcspot.spot.home.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeRequestDto(
    @SerialName("id")
    val id: Int,
    @SerialName("email")
    val email: String
)
