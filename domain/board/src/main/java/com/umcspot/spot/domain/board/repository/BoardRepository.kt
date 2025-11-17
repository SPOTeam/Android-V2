package com.umcspot.spot.domain.board.repository

import com.umcspot.spot.domain.board.model.LabeledBoardResult
import com.umcspot.spot.domain.board.model.LabeledBoardResultList
import com.umcspot.spot.domain.board.model.RankedBoardResult
import com.umcspot.spot.domain.board.model.RankedBoardResultList
import com.umcspot.spot.model.SortType

interface BoardRepository {
    suspend fun getTagBoardData(sortType : SortType): Result<RankedBoardResultList>

    suspend fun getRankedBoardData(): Result<RankedBoardResultList>

    suspend fun getLabeledBoardData(): Result<LabeledBoardResultList>

}