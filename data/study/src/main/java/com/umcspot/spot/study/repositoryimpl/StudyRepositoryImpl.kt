package com.umcspot.spot.study.repositoryimpl

import android.util.Log
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.datasource.StudyDataSource
import com.umcspot.spot.study.mapper.toData
import com.umcspot.spot.study.mapper.toDomainList
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
            response.result.toDomainList()
        }.recoverCatching {
            setPopularDummies()
        }

    private fun setPopularDummies(count: Int = 5): StudyResultList =
        StudyResultList(StudyResultList.getPopularDummies(count), hasNext = false, nextCursor = null)


    override suspend fun getRecommendStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getPopularStudies()
            response.result.toDomainList()
        }.recoverCatching {
            setRecommendDummies()
        }

    private fun setRecommendDummies(count: Int = 5): StudyResultList =
        StudyResultList(StudyResultList.getRecommendedDummies(count), hasNext = false, nextCursor = null)


    override suspend fun getRecruitingStudies(
        feeCategory: FeeRange?,
        categories: List<String>?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getRecruitingStudies(feeCategory = feeCategory, categories = categories,  sortBy = sortBy, isOnline = isOnline,  cursor = cursor, size = size)
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getRecruitingStudies failed", e)
        }.recoverCatching {
            setRecommendDummies(30)
        }

    override suspend fun getPreferLocationStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType?,
        theme: StudyTheme?,
        feeRange: FeeRange?
    ): Result<StudyResultList> = TODO()
//        runCatching {
//            val response = studyDataSource.getRecruitingStudies(
//                sortType = sortType,
//                activityType = activityType ?: ActivityType.OFFLINE,
//                theme = theme ?: StudyTheme.OTHER,
//                feeRange = feeRange ?: FeeRange.NONE
//            )
//            response.result.toDomain()
//        }

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