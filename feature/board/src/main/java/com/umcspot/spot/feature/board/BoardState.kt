package com.umcspot.spot.feature.board

import com.umcspot.spot.domain.board.model.LabeledBoardResultList
import com.umcspot.spot.domain.board.model.RankedBoardResultList
import com.umcspot.spot.model.SortType
import com.umcspot.spot.ui.state.UiState

data class BoardPayload(
    val tagBoards: RankedBoardResultList,
    val rankedBoards: RankedBoardResultList,
    val labeledBoards: LabeledBoardResultList,
    val selected: SortType
)

data class BoardUiState(
    val user: UiState<BoardPayload> = UiState.Empty
)
