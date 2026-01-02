package com.umcspot.spot.feature.board.main

import com.umcspot.spot.domain.board.model.board.BestPostResultList
import com.umcspot.spot.domain.board.model.board.RecentPostResultList
import com.umcspot.spot.model.SortType

data class BoardPayload(
    val recentBoards: RecentPostResultList,
    val bestBoards: BestPostResultList,
    val selected: SortType,
)
