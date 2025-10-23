package com.umcspot.spot.study.datasource

import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.StudyResponseDto

interface StudyDataSource {
    suspend fun getPopularStudies(): BaseResponse<StudyResponseDto>
    suspend fun getRecommendStudies(): BaseResponse<StudyResponseDto>
    suspend fun getRecruitingStudies(sortType: RecruitingStudySort): BaseResponse<StudyResponseDto>
}