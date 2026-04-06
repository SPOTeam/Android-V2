package com.umcspot.spot.study.model

data class MemoirModel(
    val memoirId: Long,
    val memberId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val activity: String,
    val learned: String,
    val encouragement: String,
    val imageUrls: List<String> = emptyList(),
    val reactionCounts: MemoirReactionCounts,
    val reactions: MemoirReactionStatus,
    val isPrivate: Boolean,
    val createdAt: String,
    val isMyMemoir: Boolean = false
)

data class MemoirReactionCounts(
    val fireCount: Int,
    val heartCount: Int,
    val starCount: Int,
    val smileCount: Int
)

data class MemoirReactionStatus(
    val isFired: Boolean,
    val isHearted: Boolean,
    val isStarred: Boolean,
    val isSmiled: Boolean
)