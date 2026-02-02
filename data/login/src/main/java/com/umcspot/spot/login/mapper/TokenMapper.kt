package com.umcspot.spot.login.mapper

import com.umcspot.spot.login.dto.request.TokenRequestDto
import com.umcspot.spot.login.dto.response.TokenResponseDto
import com.umcspot.spot.token.model.TokenResult
import com.umcspot.spot.token.model.TokenType

fun TokenType.toDto() : TokenRequestDto =
    TokenRequestDto (
        type = this.type
    )

fun TokenResponseDto.toDomain() : TokenResult =
    TokenResult (
        userId = this.userId,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
