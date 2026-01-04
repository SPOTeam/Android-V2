package com.umcspot.spot.study.repositoryimpl

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.datasource.StudyDataSource
import com.umcspot.spot.study.mapper.toData
import com.umcspot.spot.study.mapper.toDomain
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.repository.StudyRepository
import java.io.File
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyDataSource: StudyDataSource
) : StudyRepository {

    override suspend fun getPopularStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getPopularStudies()
            response.result.toDomain()
        }

    override suspend fun getRecommendStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getRecommendStudies()
            response.result.toDomain()
        }

    override suspend fun getRecruitingStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType?,
        theme: StudyTheme?,
        feeRange: FeeRange?
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getRecruitingStudies(
                sortType = sortType,
                activityType = activityType ?: ActivityType.OFFLINE,
                theme = theme ?: StudyTheme.OTHER,
                feeRange = feeRange ?: FeeRange.NONE
            )
            response.result.toDomain()
        }

    override suspend fun getPreferLocationStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType?,
        theme: StudyTheme?,
        feeRange: FeeRange?
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getRecruitingStudies(
                sortType = sortType,
                activityType = activityType ?: ActivityType.OFFLINE,
                theme = theme ?: StudyTheme.OTHER,
                feeRange = feeRange ?: FeeRange.NONE
            )
            response.result.toDomain()
        }

    override suspend fun createStudy(
        studyCreateModel: StudyCreateModel,
        imageFile: File?
    ): Result<Long> = runCatching {
        val requestDto = studyCreateModel.toData()

        val response = studyDataSource.createStudy(requestDto, imageFile)

        if (!response.isSuccess) {
            throw Exception(response.message ?: "스터디 생성 실패")
        }

        response.result.studyId
    }
}