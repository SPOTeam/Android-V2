package com.umcspot.spot.study.model

import com.umcspot.spot.model.ImageRef

data class StudyPostsResultList (
    val studyPostsList: List<StudyPostResult>,
    val hasNext: Boolean,
    val nextCursor: Long?
)

data class StudyPostResult(
    val postId: Long,
    val title: String,
    val content: String,
    val isPinned : Boolean,
    val isLiked : Boolean,
    val likeCount: Int = 0,
    val viewCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt : String,
)