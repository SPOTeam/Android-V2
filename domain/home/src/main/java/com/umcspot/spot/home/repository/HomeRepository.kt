package com.umcspot.spot.home.repository

import com.umcspot.spot.home.model.Home
import com.umcspot.spot.home.model.HomeResult

interface HomeRepository {
    suspend fun getDummies(request : Home): Result<HomeResult>
}