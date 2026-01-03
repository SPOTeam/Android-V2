package com.umcspot.spot.study.repositoryimpl

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.study.mapper.toData
import com.umcspot.spot.study.mapper.toDomainList
import com.umcspot.spot.study.model.StudyCreateModel
import com.umcspot.spot.study.model.StudyResultList
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.study.service.StudyService
import com.umcspot.spot.ui.extension.toMultipartBodyPart
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyService: StudyService
) : StudyRepository {
    override suspend fun getPopularStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyService.getPopularStudies()
            response.result!!.toDomainList()
        }.recoverCatching {
            setPopularDummies()
        }

    private fun setPopularDummies(count: Int = 5): StudyResultList =
        StudyResultList(StudyResultList.getPopularDummies(count))


    override suspend fun getRecommendStudies(): Result<StudyResultList> =
        runCatching {
            val response = studyService.getPopularStudies()
            response.result!!.toDomainList()
        }.recoverCatching {
            setRecommendDummies()
        }

    private fun setRecommendDummies(count: Int = 5): StudyResultList =
        StudyResultList(StudyResultList.getRecommendedDummies(count))


    override suspend fun getRecruitingStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType?,
        theme: StudyTheme?,
        feeRange: FeeRange?
    ): Result<StudyResultList> =
        runCatching {
            val response = studyService.getRecruitingStudies(
                sortType = sortType,
                activityType = activityType,
                theme = theme,
                feeRange = feeRange
            )
            response.result!!.toDomainList()
        }.recoverCatching {
            setRecommendDummies(30)
        }

    override suspend fun getPreferLocationStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType?,
        theme: StudyTheme?,
        feeRange: FeeRange?
    ): Result<StudyResultList> =
        runCatching {
            val response = studyService.getRecruitingStudies(
                sortType = sortType,
                activityType = activityType,
                theme = theme,
                feeRange = feeRange
            )
            response.result!!.toDomainList()
        }.recoverCatching {
            setRecommendDummies(0)
        }

    override suspend fun createStudy(
        studyCreateModel: StudyCreateModel,
        imageFile: File?
    ): Result<Long> = runCatching {
        val requestDto = studyCreateModel.toData()
        val requestPart = requestDto.toMultipartBodyPart("request")

        val imagePart = imageFile?.let { file ->
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imageFile", file.name, requestBody)
        }

        val response = studyService.createStudy(requestPart, imagePart)

        if (!response.isSuccess) {
            throw Exception(response.message ?: "스터디 생성 실패")
        }

        response.result.studyId
    }.recoverCatching { exception ->
        throw exception
    }
}