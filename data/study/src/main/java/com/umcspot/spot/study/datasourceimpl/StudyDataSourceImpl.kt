package com.umcspot.spot.study.datasourceimpl

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
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
    override suspend fun getPopularStudies(
    ): BaseResponse<StudyResponseDto> =
        studyService.getPopularStudies()

    override suspend fun getRecommendStudies(
    ): BaseResponse<StudyResponseDto> =
        studyService.getRecommendStudies()

    override suspend fun getRecruitingStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType,
        theme: StudyTheme,
        feeRange: FeeRange
    ): BaseResponse<StudyResponseDto> =
        studyService.getRecruitingStudies(sortType, activityType, theme, feeRange)

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

}