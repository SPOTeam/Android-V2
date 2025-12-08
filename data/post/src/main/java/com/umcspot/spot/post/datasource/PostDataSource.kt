package com.umcspot.spot.post.datasource

import com.umcspot.spot.network.model.BaseResponse
import com.umcspot.spot.post.dto.response.PostDetailResponseDto

interface PostDataSource {
    suspend fun getPostDetail(postId : Long): BaseResponse<PostDetailResponseDto>
}