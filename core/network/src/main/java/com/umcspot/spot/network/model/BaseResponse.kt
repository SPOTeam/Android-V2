package com.umcspot.spot.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
data class BaseResponse<T>(
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("code")
    val code : String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: T
)

@Serializable
data class NullResultResponse(
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("code")
    val code : String,
    @SerialName("message")
    val message: String
)
