package com.umcspot.spot.post.service

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.post.dto.response.FinishPostResponseDto
import com.umcspot.spot.post.dto.response.PostDetailResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostService {

    @GET("/api/posts/{postId}")
    suspend fun getPostDetail(
        @Path("postId") postId: Long,
    ): BaseResponse<PostDetailResponseDto>

    @POST("/api/posts")
    suspend fun postPost(
    ): BaseResponse<FinishPostResponseDto>

    @POST("/api/posts/{postId}/like")
    suspend fun postPostLike(
        @Path("postId") postId: Long
    ): NullResultResponse

    @DELETE("/api/posts/{postId}/unlike")
    suspend fun deletePostLike(
        @Path("postId") postId: Long
    ): NullResultResponse

}