package com.umcspot.spot.study.model

data class StudyRecentMemoirModel(
    val id: Long,
    val writerNickname: String,
    val writerProfileUrl: String?,
    val activityContent: String,
    val thumbnailUrl: String?,
    val isPrivate: Boolean
)