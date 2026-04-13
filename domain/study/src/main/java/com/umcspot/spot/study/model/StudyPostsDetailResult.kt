package com.umcspot.spot.study.model

import com.umcspot.spot.model.ImageRef


data class StudyPostDetailResult(
    val postId: Long,
    val title: String,
    val content: String,
    val isPinned : Boolean,
    val isOwner : Boolean,
    val isLiked : Boolean,

    val writerMemberId : Long,
    val writerNickname : String,
    val writerProfileUrl : ImageRef,

    val likeCount: Int = 0,
    val viewCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt : String,

    val comments : List<CommentResult>
)

data class CommentResult(
    val commentId : Long,
    val content : String,
    val isOwner : Boolean,
    val commentMemberId : Long,
    val commentNickname : String,
    val commentProfileUrl : ImageRef,
    val createdAt : String
)
