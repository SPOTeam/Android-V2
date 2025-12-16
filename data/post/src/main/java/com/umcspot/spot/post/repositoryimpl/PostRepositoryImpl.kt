package com.umcspot.spot.post.repositoryimpl

import android.content.Context
import android.net.Uri
import android.util.Log
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.network.multipart.MultipartFactory
import com.umcspot.spot.post.dto.request.RequestPosting
import com.umcspot.spot.post.mapper.toDomain
import com.umcspot.spot.post.mapper.toDto
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.post.model.postDetail.SendComment
import com.umcspot.spot.post.model.postDetail.SendCommentResult
import com.umcspot.spot.post.model.posting.Posting
import com.umcspot.spot.post.model.posting.PostingResult
import com.umcspot.spot.post.repository.PostRepository
import com.umcspot.spot.post.service.PostService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.buffer
import okio.source
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService,
    private val multipartFactory: MultipartFactory
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

    override suspend fun sendComment(
        postId: Long,
        content: SendComment
    ): Result<SendCommentResult> =
        runCatching {
            val res = postService.sendComment(postId, content.toDto())
            Log.d("PostRepository", "senedComment res = $res")
            val domain = res.result.toDomain()
            Log.d("PostRepository", "senedComment mapped = $res")
            domain
        }.onFailure { e ->
            Log.e("PostRepository", "senedComment failed", e)
        }

    override suspend fun postPost(posting: Posting): Result<PostingResult> =
        runCatching {
            val requestBody: RequestBody = RequestPosting(
                title = posting.title,
                content = posting.content,
                postType = posting.postType
            ).toJsonRequestBody()

            Log.d("PostRepository", "postPost RequestBody = $posting")

            val imagePart: MultipartBody.Part? =
                (posting.imageFile as? ImageRef.LocalUri)
                    ?.let { multipartFactory.imagePartOrNull(it.uri, formKey = "imageFile") }


            val response = postService.postPost(
                request = requestBody,
                imageFile = imagePart
            )

            // ✅ 여기부터가 핵심: 파싱 전에 상태/에러 바디 확인
            Log.d("PostRepository", "postPost http=${response.code()} success=${response.isSuccessful}")

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e("PostRepository", "postPost errorBody=$errorBody")
                throw IllegalStateException("postPost failed: http=${response.code()} errorBody=$errorBody")
            }

            val body = response.body()
                ?: throw IllegalStateException("postPost failed: empty body (http=${response.code()})")

            // BaseResponse.result 누락 케이스까지 방어
            val result = body.result
                ?: throw IllegalStateException("postPost failed: body.result is null. code=${body.code}, msg=${body.message}")

            result.toDomain()
        }.onFailure { e ->
            Log.e("PostRepository", "postPost failed", e)
        }


    override suspend fun editPost(postId: Long, posting: Posting): Result<Unit> =
        runCatching {
            val requestBody: RequestBody = RequestPosting(
                title = posting.title,
                content = posting.content,
                postType = posting.postType
            ).toJsonRequestBody()

            Log.d("PostRepository", "editPost RequestBody = $posting")

            val imagePart: MultipartBody.Part? =
                (posting.imageFile as? ImageRef.LocalUri)
                    ?.let { multipartFactory.imagePartOrNull(it.uri, formKey = "imageFile") }

            Log.d("PostRepository", "editPost RequestImagePart = $imagePart")

            val res = postService.editPost(postId, requestBody, imagePart)
            if (!res.isSuccess) error("edit failed: ${res.code} ${res.message}")
            Unit
        }

    override suspend fun deletePost(postId: Long): Result<Unit> =
        runCatching {
            postService.deletePost(postId)
        }

    override suspend fun postPostLike(postId: Long): Result<Unit> =
        runCatching {
            postService.postPostLike(postId)
        }

    override suspend fun deletePostLike(postId: Long): Result<Unit> =
        runCatching {
            postService.deletePostLike(postId)
        }

    fun getPostDetailDummy(): PostDetailResult {
        return PostDetailResult.dummyPostDetail(5, 5)
    }
}

private val json = Json { ignoreUnknownKeys = true }

fun RequestPosting.toJsonRequestBody(): RequestBody {
    val jsonString = json.encodeToString(RequestPosting.serializer(), this)
    return jsonString.toRequestBody("application/json; charset=utf-8".toMediaType())
}
