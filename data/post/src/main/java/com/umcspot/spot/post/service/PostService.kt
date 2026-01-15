package com.umcspot.spot.post.service

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.network.model.NullResultResponse
import com.umcspot.spot.post.dto.request.CommentRequestDto
import com.umcspot.spot.post.dto.request.PostingRequestDto
import com.umcspot.spot.post.dto.request.ReportPostRequestDto
import com.umcspot.spot.post.dto.response.FinishPostResponseDto
import com.umcspot.spot.post.dto.response.PostDetailResponseDto
import com.umcspot.spot.post.dto.response.SendCommentResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PostService {

    @GET("/api/posts/{postId}")
    suspend fun getPostDetail(
        @Path("postId") postId: Long,
    ): BaseResponse<PostDetailResponseDto>

    @POST("/api/posts/{postId}/like")
    suspend fun postPostLike(
        @Path("postId") postId: Long
    ): NullResultResponse

    @DELETE("/api/posts/{postId}/unlike")
    suspend fun deletePostLike(
        @Path("postId") postId: Long
    ): NullResultResponse

    @POST("/api/posts/{postId}/comments")
    suspend fun sendComment(
        @Path("postId") postId: Long,
        @Body content: CommentRequestDto
    ): BaseResponse<SendCommentResponseDto>

    @Multipart
    @POST("/api/posts")
    suspend fun postPost(
        @Part("request") request: RequestBody,
        @Part imageFile: MultipartBody.Part? = null
    ): BaseResponse<FinishPostResponseDto>

    @Multipart
    @PUT("/api/posts/{postId}")
    suspend fun editPost(
        @Path("postId") postId: Long,
        @Part("request") request: RequestBody,
        @Part imageFile: MultipartBody.Part? = null
    ): NullResultResponse

    @DELETE("/api/posts/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Long
    ): NullResultResponse

    @DELETE("/api/posts/{postId}/report")
    suspend fun reportPost(
        @Path("postId") postId: Long,
        @Body reason: ReportPostRequestDto
    ): NullResultResponse

}