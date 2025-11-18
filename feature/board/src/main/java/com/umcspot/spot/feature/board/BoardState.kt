package com.umcspot.spot.feature.board

import com.umcspot.spot.domain.board.model.LabeledBoardResultList
import com.umcspot.spot.model.SortType

data class BoardPayload(
    val tagBoards: LabeledBoardResultList,
    val labeledBoards: LabeledBoardResultList,
    val selected: SortType
)
