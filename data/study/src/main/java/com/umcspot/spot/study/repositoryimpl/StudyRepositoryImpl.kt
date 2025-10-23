package com.umcspot.spot.study.repositoryimpl

import com.umcspot.spot.model.RecruitingStudySort
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
            setPopularDummies()
        }

    private fun setPopularDummies(count: Int = 5): StudyResultList =
        StudyResultList(StudyResultList.getPopularDummies(count))


    override suspend fun getRecommendStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyService.getPopularStudies()
            response.data.toDomainList()
        }.recoverCatching {
            setRecommendDummies()
        }

    private fun setRecommendDummies(count: Int = 5): StudyResultList =
        StudyResultList(StudyResultList.getRecommendedDummies(count))


    override suspend fun getRecruitingStudies(sortType : RecruitingStudySort): Result<StudyResultList> =
        runCatching {
            val response = studyService.getRecruitingStudies(sortType = sortType)
            response.data.toDomainList()
        }.recoverCatching {
            setRecommendDummies(30)
        }

}