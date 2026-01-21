package com.umcspot.spot.study.datasourceimpl

import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.datasource.StudyDataSource
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.CreateStudyResponseDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.service.StudyService
import com.umcspot.spot.ui.extension.toMultipartBodyPart
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class StudyDataSourceImpl @Inject constructor(
    private val studyService: StudyService
) : StudyDataSource {

    override suspend fun getRecommendedStudies(
    ): BaseResponse<StudyResponseDto> =
        studyService.getRecommendedStudies()

    override suspend fun getRecruitingStudies(
        feeCategory: FeeRange?,
        categories: List<String>?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto> =
        studyService.getRecruitingStudies(feeCategory, categories, isOnline, sortBy, cursor, size)

    override suspend fun getPreferLocationStudies(
        recruitingStatus: RecruitingStatus?,
        feeCategory: FeeRange?,
        categories: List<String>?,
        sortType: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
        regionCodes: List<String>?
    ): BaseResponse<StudyResponseDto> =
        studyService.getPreferLocationStudies(recruitingStatus, feeCategory, categories, null, sortType, cursor, size, regionCodes)

    override suspend fun createStudy(
        request: StudyRequestDto,
        imageFile: File?
    ): BaseResponse<CreateStudyResponseDto> {
        val requestPart = request.toMultipartBodyPart("request")
        val imagePart = imageFile?.let { file ->
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imageFile", file.name, requestBody)
        }
        return studyService.createStudy(requestPart, imagePart)
    }

    override suspend fun getCategoryStudies(
        recruitingStatus: RecruitingStatus?,
        feeCategory: FeeRange?,
        category: String?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto> =
        studyService.getCategoryStudies(
            recruitingStatus = recruitingStatus,
            feeCategory = feeCategory,
            category = category,
            isOnline = isOnline,
            sortBy = sortBy,
            cursor = cursor,
            size = size
        )

    override suspend fun getLikedStudies(
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto> =
        studyService.getLikedStudies(
            cursor = cursor,
            size = size
        )

    override suspend fun getParticipatingStudy(
        statuses: List<String>,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto> =
        studyService.getMyPageStudy(
            statuses = statuses,
            cursor = cursor,
            size = size
        )
}