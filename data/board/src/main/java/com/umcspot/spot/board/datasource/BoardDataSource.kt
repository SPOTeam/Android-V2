package com.umcspot.spot.board.datasource

import com.umcspot.spot.board.dto.response.BestBoardResponseDto
import com.umcspot.spot.board.dto.response.PostListResponseDto
import com.umcspot.spot.board.dto.response.RecentBoardResponseDto
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse


interface BoardDataSource {
    suspend fun getRecentBoard(): BaseResponse<RecentBoardResponseDto>
    suspend fun getBestBoard(
        sortBy: SortType
    ): BaseResponse<BestBoardResponseDto>

    suspend fun getFilteredPosts(
        cursor : Long,
        postType: PostType,
        size : Int
    ): BaseResponse<PostListResponseDto>

    suspend fun postPostLike(postId : Long) : NullResultResponse

    suspend fun deletePostLike(postId : Long) : NullResultResponse

}