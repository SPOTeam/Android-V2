package com.umcspot.spot.study.model

import com.umcspot.spot.model.StudyStyle
import com.umcspot.spot.model.StudyTheme

enum class ViewerStatus {
    NOT_APPLIED,
    APPLIED,
    APPROVED,
    OWNER,
    UNKNOWN
}

data class StudyDetailModel(
    val id: Long,
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val maxMembers: Int,
    val hasFee: Boolean,
    val amount: Int,
    val categories: List<StudyTheme>,
    val styles: List<StudyStyle>,
    val regionCodes: List<String>,
    val isOnline: Boolean,
    val isLiked: Boolean,
    val totalMembers: Int,
    val currentMembers: Int,
    val likeCount: Int,
    val hitCount: Int,
    val viewerStatus: ViewerStatus
)