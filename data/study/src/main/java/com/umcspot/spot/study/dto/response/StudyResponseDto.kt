package com.umcspot.spot.study.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.study.model.ImageRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StudyResponseDto(
    @SerialName("studyList")
    val studyList : List<Study>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Study (
    @SerialName("StudyId")
    val studyId: String,

    @SerialName("title")
    val title: String,

    @SerialName("goal")
    val goal: String,

    @SerialName("maxMember")
    val maxMember : Int,

    @SerialName("member")
    val member: Int = 0,

    @SerialName("likes")
    val likes: Int = 0,

    @SerialName("views")
    val views: Int = 0,

    @SerialName("studyImage")
    val studyImage: ImageRef
)