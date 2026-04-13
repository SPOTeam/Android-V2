package com.umcspot.spot.study.dto.response

import com.umcspot.spot.model.StudyStyle
import com.umcspot.spot.model.StudyTheme
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyDetailResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("thumbnailUrl") val thumbnailUrl: String?,
    @SerialName("maxMembers") val maxMembers: Int,
    @SerialName("hasFee") val hasFee: Boolean,
    @SerialName("amount") val amount: Int,
    @SerialName("categories") val categories: List<StudyTheme>,
    @SerialName("styles") val styles: List<StudyStyle>,
    @SerialName("regionCodes") val regionCodes: List<String>,
    @SerialName("isOnline") val isOnline: Boolean,
    @SerialName("isLiked") val isLiked: Boolean,
    @SerialName("statistics") val statistics: StudyStatisticsDto,
    @SerialName("viewerStatus") val viewerStatus: String
)

@Serializable
data class StudyStatisticsDto(
    @SerialName("totalMembers") val totalMembers: Int,
    @SerialName("currentMembers") val currentMembers: Int,
    @SerialName("likeCount") val likeCount: Int,
    @SerialName("hitCount") val hitCount: Int
)