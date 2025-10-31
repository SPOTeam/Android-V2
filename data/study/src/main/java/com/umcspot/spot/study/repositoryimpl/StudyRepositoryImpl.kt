package com.umcspot.spot.study.repositoryimpl

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
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


    override suspend fun getRecruitingStudies(sortType : RecruitingStudySort, activityType: ActivityType?, theme: StudyTheme?, feeRange: FeeRange?): Result<StudyResultList> =
        runCatching {
            val response = studyService.getRecruitingStudies(sortType = sortType, activityType = activityType, theme = theme, feeRange = feeRange)
            response.data.toDomainList()
        }.recoverCatching {
            setRecommendDummies(30)
        }

    override suspend fun getPreferLocationStudies(sortType : RecruitingStudySort, activityType: ActivityType?, theme: StudyTheme?, feeRange: FeeRange?): Result<StudyResultList> =
        runCatching {
            val response = studyService.getRecruitingStudies(sortType = sortType, activityType = activityType, theme = theme, feeRange = feeRange)
            response.data.toDomainList()
        }.recoverCatching {
            setRecommendDummies(0)
        }

}