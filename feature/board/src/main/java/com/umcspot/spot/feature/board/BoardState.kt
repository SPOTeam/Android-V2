package com.umcspot.spot.feature.board

import com.umcspot.spot.domain.board.model.Board
import com.umcspot.spot.domain.board.model.Labeled
import com.umcspot.spot.model.SortType


data class BoardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val selected: SortType = SortType.LIVE,
    val hot: List<Board> = emptyList(),
    val partners: List<Labeled> = emptyList(),
    val notice: List<Board> = emptyList()
)
