package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyMemberResponseDto(
    @SerialName("members")
    val members: List<MemberDto>,
    @SerialName("totalMembers")
    val totalMembers: Int
)

@Serializable
data class MemberDto(
    @SerialName("memberId")
    val memberId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("isOwner")
    val isOwner: Boolean
)