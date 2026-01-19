package com.umcspot.spot.feature.board.main

import com.umcspot.spot.domain.board.model.board.BestPostResultList
import com.umcspot.spot.domain.board.model.board.RecentPostResultList
import com.umcspot.spot.model.SortType
import com.umcspot.spot.ui.state.UiState

data class BoardState(
    val recentBoards: UiState<RecentPostResultList> = UiState.Empty,
    val bestBoards: UiState<BestPostResultList> = UiState.Empty,
)
