package com.umcspot.spot.login.dto.request

import com.umcspot.spot.model.SocialLoginType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRequestDto(
    @SerialName("type")
    val type : SocialLoginType
)
