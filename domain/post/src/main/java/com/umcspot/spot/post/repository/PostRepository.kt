package com.umcspot.spot.post.repository

import com.umcspot.spot.post.model.postDetail.PostDetailResult

interface PostRepository {

    suspend fun getPostDetail(postId : Long): Result<PostDetailResult>

}