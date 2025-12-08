package com.umcspot.spot.post.repositoryimpl

import android.util.Log
import com.umcspot.spot.post.mapper.toDomain
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.post.repository.PostRepository
import com.umcspot.spot.post.service.PostService
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService
) : PostRepository {
    override suspend fun getPostDetail(postId : Long): Result<PostDetailResult> =
        runCatching {
            val res = postService.getPostDetail(postId)
            Log.d("PostRepository", "getRecentBoard res = $res")
            val domain = res.result.toDomain()
            Log.d("PostRepository", "getRecentBoard mapped = $domain")
            domain
        }.onFailure { e ->
            Log.e("PostRepository", "getRecentBoard failed", e)
        }.recoverCatching { e ->
            Log.w("PostRepository", "getRecentBoard using dummy because: ${e.message}")
            getPostDetailDummy()
        }

    fun getPostDetailDummy(): PostDetailResult {
        return PostDetailResult.dummyPostDetail(5, 5)
    }
}
