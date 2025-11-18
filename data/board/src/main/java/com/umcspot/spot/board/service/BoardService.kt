package com.umcspot.spot.board.service

import com.umcspot.spot.board.dto.response.LabeledBoardResponseDto
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BoardService {
    @GET("/api/posts/recent") //최근 게시글 조회
    suspend fun getTagBoardInfo(
        @Query("sortType") sortType: SortType
    ): BaseResponse<LabeledBoardResponseDto>

    @GET("/api/posts/hot") // BEST 인기글 조회
    suspend fun getLabeledBoardInfo(
    ): BaseResponse<LabeledBoardResponseDto>

}