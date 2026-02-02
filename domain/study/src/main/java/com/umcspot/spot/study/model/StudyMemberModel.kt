package com.umcspot.spot.study.model

data class StudyMemberModel(
    val id: Long,
    val name: String,
    val profileUrl: String?,
    val isLeader: Boolean
)