package com.umcspot.spot.study.datasource

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.StudyResponseDto

interface StudyDataSource {
    suspend fun getDummies(): BaseResponse<StudyResponseDto>
}