package com.umcspot.spot.post.repository

import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.post.model.postDetail.SendComment
import com.umcspot.spot.post.model.postDetail.SendCommentResult
import com.umcspot.spot.post.model.posting.Posting
import com.umcspot.spot.post.model.posting.PostingResult

interface PostRepository {
    suspend fun getPostDetail(postId : Long): Result<PostDetailResult>
    suspend fun postPostLike(postId : Long) : Result<Unit>
    suspend fun deletePostLike(postId : Long) : Result<Unit>
    suspend fun sendComment(postId : Long, content : SendComment) : Result<SendCommentResult>

    suspend fun postPost(posting : Posting): Result<PostingResult>
    suspend fun editPost(postId: Long, posting: Posting): Result<Unit>
    suspend fun deletePost(postId : Long) : Result<Unit>
}