package com.umcspot.spot.study.datasource

import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.CreateStudyResponseDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import java.io.File

interface StudyDataSource {
    suspend fun getRecommendedStudies(): BaseResponse<StudyResponseDto>
    suspend fun getRecruitingStudies(feeCategory: FeeRange?, categories: List<String>?, isOnline: Boolean?, sortBy: RecruitingStudySort?, cursor: Long?, size: Int): BaseResponse<StudyResponseDto>
    suspend fun getPreferLocationStudies(
        recruitingStatus : RecruitingStatus?,
        feeCategory: FeeRange?,
        categories: List<String>?,
        sortType: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
        regionCodes : List<String>?
    ): BaseResponse<StudyResponseDto>

    suspend fun getPreferCategoryStudies(
        category : StudyTheme?,
        recruitingStatus : RecruitingStatus?,
        feeCategory: FeeRange?,
        isOnline : Boolean?,
        sortType: RecruitingStudySort?,
        cursor: Long?,
        size: Int,
    ): BaseResponse<StudyResponseDto>

    suspend fun createStudy(request: StudyRequestDto, imageFile: File?): BaseResponse<CreateStudyResponseDto>

    suspend fun getCategoryStudies(
        recruitingStatus : RecruitingStatus?,
        feeCategory: FeeRange?,
        category: String?,
        isOnline: Boolean?,
        sortBy: RecruitingStudySort?,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto>

    suspend fun getLikedStudies(
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto>

    suspend fun getMyPageStudy(
        statuses : List<String>,
        cursor: Long?,
        size: Int
    ): BaseResponse<StudyResponseDto>
}