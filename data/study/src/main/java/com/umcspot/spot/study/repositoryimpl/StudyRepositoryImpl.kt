package com.umcspot.spot.study.repositoryimpl

import android.util.Log
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.datasource.StudyDataSource
import com.umcspot.spot.study.mapper.toData
import com.umcspot.spot.study.mapper.toDomainList
import com.umcspot.spot.study.model.StudyApplicationResultList
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.repository.StudyRepository
import java.io.File
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyDataSource: StudyDataSource,
) : StudyRepository {

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
            val response = studyDataSource.getRecruitingStudies(
                feeCategory = feeCategory,
                categories = categories,
                sortBy = sortBy,
                isOnline = isOnline,
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getRecruitingStudies failed", e)
        }.recoverCatching {
            setRecommendDummies(30)
        }

    override suspend fun getPreferLocationStudies(
        recruitingStatus: RecruitingStatus?,
        feeRange: FeeRange?,
        categories: List<String>?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
        regionCodes : List<String>?
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getPreferLocationStudies(
                recruitingStatus = recruitingStatus,
                feeCategory = feeRange,
                categories = categories,
                sortType = sortBy,
                cursor = cursor,
                size = size,
                regionCodes = regionCodes
            )
            response.result.toDomainList()
        }.onFailure {
            Log.e("StudyRepository", "getPreferLocationStudies failed", it)
        }

    override suspend fun getPreferCategoryStudies(
        category: StudyTheme?,
        recruitingStatus: RecruitingStatus?,
        feeRange: FeeRange?,
        isOnline : Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getPreferCategoryStudies(
                category = category,
                recruitingStatus = recruitingStatus,
                feeCategory = feeRange,
                isOnline = isOnline,
                sortType = sortBy,
                cursor = cursor,
                size = size,
            )
            response.result.toDomainList()
        }.onFailure {
            Log.e("StudyRepository", "getPreferCategoryStudies failed", it)
        }

    override suspend fun getRecommendedStudies(): Result<StudyResultList> =
        runCatching {
            studyDataSource.getRecommendedStudies().result.toDomainList()
        }.onFailure {
            Log.e("StudyRepository", "getRecommendedStudies failed", it)
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

    override suspend fun getCategoryStudies(
        recruitingStatus: RecruitingStatus?,
        feeRange: FeeRange?,
        category: String?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort,
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getCategoryStudies(
                recruitingStatus = recruitingStatus,
                feeCategory = feeRange,
                category = category,
                sortBy = sortBy,
                isOnline = isOnline,
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getCategoryStudies failed", e)
        }

    override suspend fun getLikedStudies(
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getLikedStudies(
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getLikedStudies failed", e)
        }

    override suspend fun getParticipatingStudy(
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getMyPageStudy(
                statuses = listOf("OWNER","APPROVED"),
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getParticipatingStudy failed", e)
        }

    override suspend fun getRecruitingStudy(
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getMyPageStudy(
                statuses = listOf("OWNER"),
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getRecruitingStudy failed", e)
        }

    override suspend fun getWaitingStudy(
        cursor: Long?,
        size: Int
    ): Result<StudyResultList> =
        runCatching {
            val response = studyDataSource.getMyPageStudy(
                statuses = listOf("APPLIED"),
                cursor = cursor,
                size = size
            )
            response.result.toDomainList()
        }.onFailure { e ->
            Log.e("StudyRepository", "getRecruitingStudy failed", e)
        }

    override suspend fun getStudyApplications(studyId: Long): Result<StudyApplicationResultList> =
        runCatching {
            val response = studyDataSource.getStudyApplications(studyId)
            response.result.toDomainList()
        }.onFailure {
            Log.e("StudyRepository", "getStudyApplicationMembers failed", it)
        }

    override suspend fun entryAcceptance(
        applicationId: Long,
        decision: String
    ): Result<Unit> =
        runCatching {
            studyDataSource.entryAcceptance(applicationId, decision)
        }
}