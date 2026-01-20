package com.umcspot.spot.post.datasource

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.post.dto.request.CommentRequestDto
import com.umcspot.spot.post.dto.request.ReportPostRequestDto
import com.umcspot.spot.post.dto.response.FinishPostResponseDto
import com.umcspot.spot.post.dto.response.PostDetailResponseDto
import com.umcspot.spot.post.dto.response.SendCommentResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface PostDataSource {
    suspend fun getPostDetail(postId : Long): BaseResponse<PostDetailResponseDto>

    suspend fun postPostLike(postId : Long) : NullResultResponse

    suspend fun deletePostLike(postId : Long) : NullResultResponse

    suspend fun deletePost(postId : Long) : NullResultResponse

    suspend fun sendComment(postId : Long, comment : CommentRequestDto) : BaseResponse<SendCommentResponseDto>
    suspend fun postPost(request : RequestBody, imageFile: MultipartBody.Part?): BaseResponse<FinishPostResponseDto>
    suspend fun editPost(postId: Long, request: RequestBody, imageFile: MultipartBody.Part?): NullResultResponse
    suspend fun reportPost(postId : Long, request : ReportPostRequestDto) : NullResultResponse
}