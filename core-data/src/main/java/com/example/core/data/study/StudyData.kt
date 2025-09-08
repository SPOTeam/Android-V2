package com.example.core.data.study

import androidx.annotation.DrawableRes

data class StudyItem(
    val id: String,
    val title: String,
    val goal: String,
    val maxMember : Int,
    val member: Int = 0,
    val likes: Int = 0,
    val views: Int = 0,
    @DrawableRes val studyImage: Int? = null
)
