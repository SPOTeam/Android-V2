package com.umcspot.spot.study.datasource

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.CreateStudyResponseDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import java.io.File

interface StudyDataSource {
    suspend fun getPopularStudies(): BaseResponse<StudyResponseDto>
    suspend fun getRecommendStudies(): BaseResponse<StudyResponseDto>
    suspend fun getRecruitingStudies(
        sortType: RecruitingStudySort,
        activityType: ActivityType,
        theme: StudyTheme,
        feeRange: FeeRange
    ): BaseResponse<StudyResponseDto>
    suspend fun createStudy(request: StudyRequestDto, imageFile: File?): BaseResponse<CreateStudyResponseDto>
}