package com.umcspot.spot.post.mapper

import com.umcspot.spot.model.toImageRef
import com.umcspot.spot.post.dto.response.CommentResponse
import com.umcspot.spot.post.dto.response.PostDetailResponseDto
import com.umcspot.spot.post.model.postDetail.CommentResult
import com.umcspot.spot.post.model.postDetail.PostDetailResult

fun PostDetailResponseDto.toDomain(): PostDetailResult =
    PostDetailResult(
        postId = this.postId,
        title = this.title,
        content = this.content,
        imageUrl = this.imageUrl.toImageRef(),
        postType = this.postType,
        isLiked = this.isLiked,
        writerId = this.writer.writerId,
        nickname = this.writer.nickname,
        profileImageUrl = this.writer.profileImageUrl.toImageRef(),
        likeCount = this.stats.likeCount,
        viewCount = this.stats.viewCount,
        commentCount = this.stats.commentCount,
        createdAt = this.createdAt,
        comments = this.comments.map { it.toDomain() }
    )


fun CommentResponse.toDomain() : CommentResult =
    CommentResult(
        commentId = this.commentId,
        content = this.content,
        writerId = this.writer.writerId,
        nickname = this.writer.nickname,
        profileImageUrl = this.writer.profileImageUrl.toImageRef(),
        createdAt = this.createdAt
    )
