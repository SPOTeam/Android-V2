package com.umcspot.spot.study.repositoryimpl

import com.umcspot.spot.study.mapper.toDomainList
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.study.service.StudyService
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyService: StudyService
) : StudyRepository {
    override suspend fun getPopularStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyService.getPopularStudies()
            response.data.toDomainList()
        }.recoverCatching {
            // API 미연결/예외 시 더미로 복구
            setPopularDummies()
        }

    private fun setPopularDummies(count: Int = 5): StudyResultList =
        StudyResultList(StudyResultList.getPopularDummies(count))


    override suspend fun getRecommendStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyService.getPopularStudies()
            response.data.toDomainList()
        }.recoverCatching {
            // API 미연결/예외 시 더미로 복구
            setRecommendDummies()
        }

    private fun setRecommendDummies(count: Int = 5): StudyResultList =
        StudyResultList(StudyResultList.getRecommendedDummies(count))

}