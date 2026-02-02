package com.umcspot.spot.study.model

data class MemoirCreateModel(
    val activity: String,
    val learned: String,
    val encouragement: String,
    val isPrivate: Boolean
)