package com.umcspot.spot.domain.board.repository

import com.umcspot.spot.domain.board.model.LabeledBoardResultList
import com.umcspot.spot.model.SortType

interface BoardRepository {

    suspend fun getLabeledBoardData(): Result<LabeledBoardResultList>

    suspend fun getTagBoardData(sortType : SortType): Result<LabeledBoardResultList>
}