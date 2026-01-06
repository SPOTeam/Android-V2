package com.umcspot.spot.study.service

import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.RecruitingStatus
import com.umcspot.spot.model.RecruitingStudySort
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.study.dto.response.CreateStudyResponseDto
import com.umcspot.spot.study.dto.response.StudyResponseDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface StudyService {
    @GET("/api/v1/service")
    suspend fun getPopularStudies(
//        @Body request: StudyRequestDto
    ): BaseResponse<StudyResponseDto>

    @GET("/api/v1/service")
    suspend fun getRecommendStudies(
//        @Body request: StudyRequestDto
    ): BaseResponse<StudyResponseDto>

    @GET("/api/studies/recruiting")
    suspend fun getRecruitingStudies(
        @Query("feeCategory") feeCategory: FeeRange?,
        @Query("categories") categories:  List<String>?,
        @Query("isOnline") isOnline: Boolean?,
        @Query("sortBy") sortBy: RecruitingStudySort?,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int
    ): BaseResponse<StudyResponseDto>

    @GET("/api/studies/by-region")
    suspend fun getPreferLocationStudies(
        @Query("recruitingStatus") recruitingStatus: RecruitingStatus?,
        @Query("feeCategory") feeCategory: FeeRange?,
        @Query("categories") categories: List<String>?,
        @Query("isOnline") isOnline: Boolean?,
        @Query("sortBy") sortBy: RecruitingStudySort?,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int,
        @Query("regionCodes") regionCodes: List<String>?
    ): BaseResponse<StudyResponseDto>

    @Multipart
    @POST("/api/studies")
    suspend fun createStudy(
        @Part request: MultipartBody.Part,
        @Part imageFile: MultipartBody.Part?
    ): BaseResponse<CreateStudyResponseDto>
}