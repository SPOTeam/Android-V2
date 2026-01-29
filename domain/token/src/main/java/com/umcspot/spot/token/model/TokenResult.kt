package com.umcspot.spot.token.model

data class TokenResult (
    val userId : String,
    val accessToken : String,
    val refreshToken : String,
)