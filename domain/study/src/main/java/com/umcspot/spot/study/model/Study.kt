package com.umcspot.spot.study.model

sealed interface ImageRef {
    data object None : ImageRef
    data class Url(val url: String) : ImageRef
    data class LocalName(val name: String) : ImageRef // "ic_study_default" 같은 이름
}

data class StudyItem(
    val id: String,
    val title: String,
    val goal: String,
    val maxMember : Int,
    val member: Int = 0,
    val likes: Int = 0,
    val views: Int = 0,
    val studyImage: ImageRef = ImageRef.None
)
