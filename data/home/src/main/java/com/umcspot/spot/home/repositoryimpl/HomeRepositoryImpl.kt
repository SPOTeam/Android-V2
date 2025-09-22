package com.umcspot.spot.home.repositoryimpl

import com.umcspot.spot.home.mapper.toData
import com.umcspot.spot.home.mapper.toDomain
import com.umcspot.spot.home.model.Dummy
import com.umcspot.spot.home.model.DummyResult
import com.umcspot.spot.home.repository.HomeRepository
import com.umcspot.spot.home.service.HomeService
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService
) : HomeRepository {
    override suspend fun getDummies(request: Dummy): Result<DummyResult> =
        runCatching {
            val response = homeService.getDummies(request = request.toData())
            response.data.toDomain()
        }
}