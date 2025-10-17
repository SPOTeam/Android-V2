package com.umcspot.spot.study.service

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.dto.request.StudyRequestDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import retrofit2.http.Body
import retrofit2.http.GET

interface StudyService {
    @GET("/api/v1/service")
    suspend fun getPopularStudies(
//        @Body request: StudyRequestDto
    ): BaseResponse<StudyResponseDto>

    @GET("/api/v1/service")
    suspend fun getRecommendStudies(
//        @Body request: StudyRequestDto
    ): BaseResponse<StudyResponseDto>


}