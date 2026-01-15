package com.umcspot.spot.post.repositoryimpl

import android.util.Log
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.network.multipart.MultipartFactory
import com.umcspot.spot.post.datasource.PostDataSource
import com.umcspot.spot.post.dto.request.RequestPosting
import com.umcspot.spot.post.mapper.toDomain
import com.umcspot.spot.post.mapper.toDto
import com.umcspot.spot.post.mapper.toSendDto
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.post.model.postDetail.ReportPostReason
import com.umcspot.spot.post.model.postDetail.SendComment
import com.umcspot.spot.post.model.postDetail.SendCommentResult
import com.umcspot.spot.post.model.posting.Posting
import com.umcspot.spot.post.model.posting.PostingResult
import com.umcspot.spot.post.repository.PostRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource,
    private val multipartFactory: MultipartFactory
) : PostRepository {
    override suspend fun getPostDetail(postId : Long): Result<PostDetailResult> =
        runCatching {
            val res = postDataSource.getPostDetail(postId)
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
            val res = postDataSource.sendComment(postId, content.toSendDto())
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


            val response = postDataSource.postPost(
                request = requestBody,
                imageFile = imagePart
            )

            response.result.toDomain()
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

            val res = postDataSource.editPost(postId, requestBody, imagePart)
            if (!res.isSuccess) error("edit failed: ${res.code} ${res.message}")
            Unit
        }

    override suspend fun deletePost(postId: Long): Result<Unit> =
        runCatching {
            postDataSource.deletePost(postId)
        }

    override suspend fun reportPost(
        postId: Long,
        reason: ReportPostReason
    ): Result<Unit> =
        runCatching {
            postDataSource.reportPost(postId, reason.toDto())
            Unit
        }

    override suspend fun postPostLike(postId: Long): Result<Unit> =
        runCatching {
            postDataSource.postPostLike(postId)
        }

    override suspend fun deletePostLike(postId: Long): Result<Unit> =
        runCatching {
            postDataSource.deletePostLike(postId)
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
