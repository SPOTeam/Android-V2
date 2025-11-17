package com.umcspot.spot.study.dto.request

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class StudyRequestDto(
    @SerialName("studyId")
    val studyId : Int
)
