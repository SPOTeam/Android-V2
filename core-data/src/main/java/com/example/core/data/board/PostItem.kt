package com.example.core.data.board

import com.example.core.data.global.SortType

data class BoardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val selected: SortType = SortType.LIVE,
    val hot: List<BoardItem> = emptyList(),
    val partners: List<LabeledItem> = emptyList(),
    val notice: List<BoardItem> = emptyList()
)

data class BoardItem(
    val id: String,
    val title: String,
    val count: Int
)

data class LabeledItem(
    val id: String,
    val label: String,
    val title: String,
    val count: Int
)