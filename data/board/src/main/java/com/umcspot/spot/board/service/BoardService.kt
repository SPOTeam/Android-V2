package com.umcspot.spot.board.service

import com.umcspot.spot.board.dto.response.BestBoardResponseDto
import com.umcspot.spot.board.dto.response.PostResponseDto
import com.umcspot.spot.board.dto.response.RecentBoardResponseDto
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BoardService {

    @GET("/api/posts/recent") //최근 게시글 조회
    suspend fun getRecentBoard(
    ): BaseResponse<RecentBoardResponseDto>

    @GET("/api/posts/hot") // BEST 인기글 조회
    suspend fun getBestBoard(
        @Query("sortBy") sortBy: SortType
    ): BaseResponse<BestBoardResponseDto>

    @GET("/api/posts") // 전체 글 조회
    suspend fun getFilteredPosts(
        @Query("cursor") cursor: Long?,
        @Query("postType") postType: PostType?,
        @Query("size") size: Int,
    ): BaseResponse<PostResponseDto>
}