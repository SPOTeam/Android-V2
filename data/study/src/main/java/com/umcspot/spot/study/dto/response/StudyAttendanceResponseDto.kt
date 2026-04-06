package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyAttendanceResponseDto(
    @SerialName("attendances")
    val attendances: List<AttendanceDto>,
    @SerialName("totalCount")
    val totalCount: Int
)

@Serializable
data class AttendanceDto(
    @SerialName("member")
    val member: AttendanceMemberDto,
    @SerialName("attendanceStatus")
    val attendanceStatus: String,
    @SerialName("attendedAt")
    val attendedAt: String?
)

@Serializable
data class AttendanceMemberDto(
    @SerialName("memberId")
    val memberId: String,
    @SerialName("memberName")
    val memberName: String,
    @SerialName("memberProfileImageUrl")
    val memberProfileImageUrl: String
)