package com.umcspot.spot.study.repositoryimpl

import com.umcspot.spot.study.mapper.toData
import com.umcspot.spot.study.mapper.toDomain
import com.umcspot.spot.study.model.Study
import com.umcspot.spot.study.model.StudyResult
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.study.service.StudyService
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyService: StudyService
) : StudyRepository {
    override suspend fun getDummies(): Result<StudyResult> =
        runCatching {
            val response = studyService.getDummies()
            response.data.toDomain()
        }
}