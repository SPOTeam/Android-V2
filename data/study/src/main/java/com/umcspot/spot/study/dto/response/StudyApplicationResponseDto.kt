package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyApplicationResponseDto(
    @SerialName("applies")
    val applies: List<StudyApplication>
)

@Serializable
data class StudyApplication(
    @SerialName("applicantId")
    val applicantId: String,
    @SerialName("memberId")
    val memberId: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("description")
    val description: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?
)