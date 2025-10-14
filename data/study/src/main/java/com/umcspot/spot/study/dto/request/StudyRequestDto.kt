package com.umcspot.spot.study.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyRequestDto(
    @SerialName("studyId")
    val studyId : Int
)
