package com.umcspot.spot.post.mapper

import com.umcspot.spot.model.formatCreatedAt
import com.umcspot.spot.model.toImageRef
import com.umcspot.spot.post.dto.request.CommentRequestDto
import com.umcspot.spot.post.dto.request.PostingRequestDto
import com.umcspot.spot.post.dto.request.ReportPostRequestDto
import com.umcspot.spot.post.dto.request.RequestPosting
import com.umcspot.spot.post.dto.response.CommentResponse
import com.umcspot.spot.post.dto.response.FinishPostResponseDto
import com.umcspot.spot.post.dto.response.PostDetailResponseDto
import com.umcspot.spot.post.dto.response.SendCommentResponseDto
import com.umcspot.spot.post.model.postDetail.CommentResult
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.post.model.postDetail.ReportPostReason
import com.umcspot.spot.post.model.postDetail.SendComment
import com.umcspot.spot.post.model.postDetail.SendCommentResult
import com.umcspot.spot.post.model.posting.Posting
import com.umcspot.spot.post.model.posting.PostingResult

fun PostDetailResponseDto.toDomain(): PostDetailResult =
    PostDetailResult(
        postId = this.postId,
        title = this.title,
        content = this.content,
        imageUrl = this.imageUrl.toImageRef(),
        postType = this.postType,
        isLiked = this.isLiked,
        isOwner = this.isOwner,
        writerId = this.writer.writerId,
        nickname = this.writer.nickname,
        profileImageUrl = this.writer.profileImageUrl.toImageRef(),
        likeCount = this.stats.likeCount,
        viewCount = this.stats.viewCount,
        commentCount = this.stats.commentCount,
        createdAt = this.createdAt.formatCreatedAt(),
        comments = this.comments.map { it.toDomain() }
    )


fun CommentResponse.toDomain() : CommentResult =
    CommentResult(
        commentId = this.commentId,
        content = this.content,
        writerId = this.writer.writerId,
        nickname = this.writer.nickname,
        profileImageUrl = this.writer.profileImageUrl.toImageRef(),
        createdAt = this.createdAt.formatCreatedAt()
    )

fun SendComment.toDto() : CommentRequestDto =
    CommentRequestDto(
        content = this.content
    )

fun SendCommentResponseDto.toDomain() : SendCommentResult =
    SendCommentResult(
        commentId = this.commentId
    )

fun Posting.toDto(): PostingRequestDto =
    PostingRequestDto(
        request = RequestPosting(
            title = this.title,
            content = this.content,
            postType = this.postType
        ),
        imageFile = this.imageFile
    )

fun SendComment.toSendDto(): CommentRequestDto =
    CommentRequestDto(
        content = this.content
    )

fun ReportPostReason.toDto(): ReportPostRequestDto =
    ReportPostRequestDto(
        reason = this.reason
    )

fun ReportPostRequestDto.toDto(): ReportPostRequestDto =
    ReportPostRequestDto(
        reason = this.reason
    )


fun FinishPostResponseDto.toDomain(): PostingResult =
    PostingResult(
        postId = this.postId
    )