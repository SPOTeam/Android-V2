package com.umcspot.spot.study.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyMemoirResponseDto(
    @SerialName("reviews") val memoirs: List<MemoirDto>,
    @SerialName("hasNext") val hasNext: Boolean,
    @SerialName("nextCursor") val nextCursor: Long?,
    @SerialName("totalElements") val totalElements: Int
)

@Serializable
data class MemoirDto(
    @SerialName("reviewId") val memoirId: Long,
    @SerialName("writer") val writer: MemoirWriterDto,
    @SerialName("content") val content: MemoirContentDto,
    @SerialName("reactionCounts") val reactionCounts: MemoirReactionCountsDto,
    @SerialName("reactions") val reactions: MemoirReactionStatusDto,
    @SerialName("isPrivate") val isPrivate: Boolean,
    @SerialName("createdAt") val createdAt: String
)

@Serializable
data class MemoirWriterDto(
    @SerialName("memberId") val memberId: Long,
    @SerialName("nickname") val nickname: String,
    @SerialName("profileImageUrl") val profileImageUrl: String? = null
)

@Serializable
data class MemoirContentDto(
    @SerialName("activity") val activity: String,
    @SerialName("learned") val learned: String,
    @SerialName("encouragement") val encouragement: String,
    @SerialName("imageUrls") val imageUrls: List<String> = emptyList()
)

@Serializable
data class MemoirReactionCountsDto(
    @SerialName("fireCount") val fireCount: Int,
    @SerialName("heartCount") val heartCount: Int,
    @SerialName("starCount") val starCount: Int,
    @SerialName("smileCount") val smileCount: Int
)

@Serializable
data class MemoirReactionStatusDto(
    @SerialName("isFired") val isFired: Boolean,
    @SerialName("isHearted") val isHearted: Boolean,
    @SerialName("isStarred") val isStarred: Boolean,
    @SerialName("isSmiled") val isSmiled: Boolean
)