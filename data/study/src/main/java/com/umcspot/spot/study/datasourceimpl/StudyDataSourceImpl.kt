package com.umcspot.spot.study.datasourceimpl

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.datasource.StudyDataSource
import com.umcspot.spot.study.dto.response.StudyResponseDto
import com.umcspot.spot.study.service.StudyService
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
}