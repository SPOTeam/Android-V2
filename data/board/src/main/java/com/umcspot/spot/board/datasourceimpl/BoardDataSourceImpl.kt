package com.umcspot.spot.board.datasourceimpl

import com.umcspot.spot.board.datasource.BoardDataSource
import com.umcspot.spot.board.dto.response.LabeledBoardResponseDto
import com.umcspot.spot.board.service.BoardService
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.BaseResponse
import javax.inject.Inject

class BoardDataSourceImpl @Inject constructor(
    private val boardService: BoardService
) : BoardDataSource {
    override suspend fun geTagBoardInfo(
        sortType : SortType
    ): BaseResponse<LabeledBoardResponseDto> =
        boardService.getTagBoardInfo(sortType)


    override suspend fun geLabeledBoardInfo(
    ): BaseResponse<LabeledBoardResponseDto> =
        boardService.getLabeledBoardInfo()
}