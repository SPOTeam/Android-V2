package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyDetailResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("thumbnailUrl") val thumbnailUrl: String?,
    @SerialName("categories") val categories: List<String>,
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