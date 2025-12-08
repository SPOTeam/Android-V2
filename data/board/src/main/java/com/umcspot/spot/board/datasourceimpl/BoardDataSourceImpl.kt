package com.umcspot.spot.board.datasourceimpl

import com.umcspot.spot.board.datasource.BoardDataSource
import com.umcspot.spot.board.dto.response.BestBoardResponseDto
import com.umcspot.spot.board.dto.response.PostResponseDto
import com.umcspot.spot.board.dto.response.RecentBoardResponseDto
import com.umcspot.spot.board.service.BoardService
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.BaseResponse
import javax.inject.Inject

class BoardDataSourceImpl @Inject constructor(
    private val boardService: BoardService
) : BoardDataSource {
    override suspend fun getRecentBoard( ): BaseResponse<RecentBoardResponseDto> =
        boardService.getRecentBoard()


    override suspend fun getBestBoard(
        sortBy: SortType
    ): BaseResponse<BestBoardResponseDto> =
        boardService.getBestBoard(sortBy)

    override suspend fun getFilteredPosts(
        cursor : Long,
        postType: PostType,
        size : Int
    ): BaseResponse<PostResponseDto> =
        boardService.getFilteredPosts(cursor, postType, size)

}