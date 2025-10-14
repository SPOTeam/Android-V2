package com.umcspot.spot.domain.board.model

data class Board(
    val id: String,
    val title: String,
    val count: Int
)

data class Labeled(
    val id: String,
    val label: String,
    val title: String,
    val count: Int
)