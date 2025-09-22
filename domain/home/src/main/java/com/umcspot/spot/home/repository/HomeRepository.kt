package com.umcspot.spot.home.repository

import com.umcspot.spot.home.model.Dummy
import com.umcspot.spot.home.model.DummyResult

interface HomeRepository {
    suspend fun getDummies(request: Dummy): Result<DummyResult>
}