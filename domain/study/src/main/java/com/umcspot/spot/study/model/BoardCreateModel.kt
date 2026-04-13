package com.umcspot.spot.study.model

data class BoardCreateModel(
    val title: String,
    val content: String,
    val isPrivate: Boolean
)