package com.umcspot.spot.study.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyWithdrawRequestDto(
    @SerialName("withdrawReason")
    val withdrawReason: String,
    @SerialName("nextOwnerId")
    val nextOwnerId: Long?
)