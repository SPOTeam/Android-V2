@file:OptIn(InternalSerializationApi::class)

package com.umcspot.spot.datastore.token

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class SpotTokenData(
    val accessToken: String = "",
    val refreshToken: String = "",
)