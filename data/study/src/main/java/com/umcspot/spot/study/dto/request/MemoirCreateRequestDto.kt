package com.umcspot.spot.study.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemoirCreateRequestDto(
    @SerialName("activity") val activity: String,
    @SerialName("learned") val learned: String,
    @SerialName("encouragement") val encouragement: String,
    @SerialName("isPrivate") val isPrivate: Boolean
)