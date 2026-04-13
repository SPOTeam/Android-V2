package com.umcspot.spot.study.detail.model

import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.study.model.MemoirModel
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.study.model.StudyPostResult
import com.umcspot.spot.study.model.StudyPostsResultList
import com.umcspot.spot.study.model.StudyRecentMemoirModel
import com.umcspot.spot.study.model.StudyScheduleModel
import com.umcspot.spot.study.model.TodoModel
import com.umcspot.spot.ui.state.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

data class StudyPostContentState(
    val postData: UiState<PostData> = UiState.Empty,
    val commentData : ImmutableList<CommentData> = persistentListOf()
)

data class PostData(
    val postId : Long,
    val title : String,
    val content : String,
    val isPinned : Boolean,
    val isLiked : Boolean,
    val writerId : Long,
    val nickname : String,
    val profileImageUrl : ImageRef,
    val likeCount : Int,
    val viewCount : Int,
    val commentCount : Int,
    val createdAt : String
)

data class CommentData(
    val commentId : Long,
    val content : String,
    val isOwner : Boolean,
    val writerId : Long,
    val nickname : String,
    val profileImageUrl : ImageRef,
    val createdAt : String,
)

