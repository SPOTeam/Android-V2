package com.umcspot.spot.board.service

import com.umcspot.spot.board.dto.response.LabeledBoardResponseDto
import com.umcspot.spot.board.dto.response.RankedBoardResponseDto
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BoardService {
    @GET("/api/v1/service")
    suspend fun getTagBoardInfo(
        @Query("sortType") sortType: SortType
    ): BaseResponse<RankedBoardResponseDto>

    @GET("/api/v1/service")
    suspend fun getRankedBoardInfo(
    ): BaseResponse<RankedBoardResponseDto>

    @GET("/api/v1/service")
    suspend fun getLabeledBoardInfo(
    ): BaseResponse<LabeledBoardResponseDto>

}