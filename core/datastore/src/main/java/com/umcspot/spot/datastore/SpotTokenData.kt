package com.umcspot.spot.datastore


import kotlinx.serialization.Serializable

@Serializable
data class SpotTokenData(
    val accessToken: String = "",
    val refreshToken: String = "",
)