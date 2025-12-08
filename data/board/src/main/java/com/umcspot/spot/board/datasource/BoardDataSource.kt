package com.umcspot.spot.board.datasource

import com.umcspot.spot.board.dto.response.BestBoardResponseDto
import com.umcspot.spot.board.dto.response.PostResponseDto
import com.umcspot.spot.board.dto.response.RecentBoardResponseDto
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.BaseResponse


interface BoardDataSource {
    suspend fun getRecentBoard(): BaseResponse<RecentBoardResponseDto>
    suspend fun getBestBoard(
        sortBy: SortType
    ): BaseResponse<BestBoardResponseDto>

    suspend fun getFilteredPosts(
        cursor : Long,
        postType: PostType,
        size : Int
    ): BaseResponse<PostResponseDto>

}