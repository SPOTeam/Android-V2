package com.umcspot.spot.post.datasource

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.post.dto.request.CommentRequestDto
import com.umcspot.spot.post.dto.response.PostDetailResponseDto
import com.umcspot.spot.post.dto.response.SendCommentResponseDto
import com.umcspot.spot.post.model.postDetail.SendComment

interface PostDataSource {
    suspend fun getPostDetail(postId : Long): BaseResponse<PostDetailResponseDto>

    suspend fun postPostLike(postId : Long) : NullResultResponse

    suspend fun deletePostLike(postId : Long) : NullResultResponse

    suspend fun deletePost(postId : Long) : NullResultResponse

    suspend fun sendComment(postId : Long, comment : CommentRequestDto) : BaseResponse<SendCommentResponseDto>
}