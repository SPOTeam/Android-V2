package com.umcspot.spot.post.datasourceimpl

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.post.datasource.PostDataSource
import com.umcspot.spot.post.dto.response.PostDetailResponseDto
import com.umcspot.spot.post.service.PostService
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val postService: PostService
) : PostDataSource {
    override suspend fun getPostDetail(postId : Long): BaseResponse<PostDetailResponseDto> =
        postService.getPostDetail(postId)
}