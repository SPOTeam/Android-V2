@file:OptIn(InternalSerializationApi::class)

package com.umcspot.spot.datastore.userId

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class SpotUserIdData(
    val userId: String = "",
)