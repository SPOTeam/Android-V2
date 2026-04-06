package com.umcspot.spot.study.model

data class StudyAttendanceListModel(
    val attendances: List<StudyAttendanceModel>,
    val totalCount: Int
)

data class StudyAttendanceModel(
    val memberId: String,
    val name: String,
    val profileUrl: String,
    val status: AttendanceStatus,
    val attendedAt: String?
)

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    UNDECIDED
}