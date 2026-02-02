package com.umcspot.spot.post.datasourceimpl

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.post.datasource.PostDataSource
import com.umcspot.spot.post.dto.request.CommentRequestDto
import com.umcspot.spot.post.dto.request.ReportPostRequestDto
import com.umcspot.spot.post.dto.response.FinishPostResponseDto
import com.umcspot.spot.post.dto.response.PostDetailResponseDto
import com.umcspot.spot.post.dto.response.SendCommentResponseDto
import com.umcspot.spot.post.model.posting.Posting
import com.umcspot.spot.post.service.PostService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val postService: PostService
) : PostDataSource {
    override suspend fun getPostDetail(postId : Long): BaseResponse<PostDetailResponseDto> =
        postService.getPostDetail(postId)

    override suspend fun postPostLike(postId: Long): NullResultResponse =
        postService.postPostLike(postId)

    override suspend fun deletePostLike(postId: Long): NullResultResponse =
        postService.deletePostLike(postId)

    override suspend fun deletePost(postId: Long): NullResultResponse =
        postService.deletePost(postId)

    override suspend fun sendComment(
        postId: Long,
        comment: CommentRequestDto
    ): BaseResponse<SendCommentResponseDto> =
        postService.sendComment(postId, comment)

    override suspend fun postPost(
        request: RequestBody,
        imageFile: MultipartBody.Part?
    ): BaseResponse<FinishPostResponseDto> =
        postService.postPost(request, imageFile)

    override suspend fun editPost(
        postId: Long,
        request: RequestBody,
        imageFile: MultipartBody.Part?
    ): NullResultResponse =
        postService.editPost(postId, request, imageFile)

    override suspend fun reportPost(
        postId: Long,
        reason: ReportPostRequestDto
    ): NullResultResponse =
        postService.reportPost(postId, reason)

}