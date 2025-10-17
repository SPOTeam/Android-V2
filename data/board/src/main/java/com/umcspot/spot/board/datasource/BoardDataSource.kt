package com.umcspot.spot.board.datasource

import com.umcspot.spot.board.dto.response.LabeledBoardResponseDto
import com.umcspot.spot.board.dto.response.RankedBoardResponseDto
import com.umcspot.spot.model.SortType
import com.umcspot.spot.network.model.BaseResponse


interface BoardDataSource {
    suspend fun geTagBoardInfo(sortType : SortType): BaseResponse<RankedBoardResponseDto>
    suspend fun geRankedBoardInfo(): BaseResponse<RankedBoardResponseDto>
    suspend fun geLabeledBoardInfo(): BaseResponse<LabeledBoardResponseDto>
}