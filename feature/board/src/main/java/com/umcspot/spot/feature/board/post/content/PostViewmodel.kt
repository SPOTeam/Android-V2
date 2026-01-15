package com.umcspot.spot.feature.board.post.content

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.feature.board.boardList.BoardListViewModel.BoardUiState
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.post.model.postDetail.ReportPostReason
import com.umcspot.spot.post.model.postDetail.SendComment
import com.umcspot.spot.post.repository.PostRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    data class PostDetailUiState(
        val data: UiState<PostDetailResult> = UiState.Empty
    )

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private val _likeSelected = MutableStateFlow(false)
    val likeSelected: StateFlow<Boolean> = _likeSelected.asStateFlow()

    private val inFlightLikes = mutableSetOf<Long>()


    fun load(postId: Long) {
        _uiState.update { it.copy(data = UiState.Loading) }

        viewModelScope.launch {
            runCatching {
                postRepository.getPostDetail(
                    postId = postId
                ).getOrThrow()
            }.onSuccess { data ->
                Log.d("PostViewModel", "load postDetail: $data")
                _uiState.update { it.copy(data = UiState.Success(data)) }
            }.onFailure { e ->
                Log.e("PostViewModel", "load error", e)
                _uiState.update { it.copy(data = UiState.Empty) }
            }
        }
    }

    fun toggleLike() {
        val current = (_uiState.value.data as? UiState.Success)?.data ?: return
        val id = current.postId
        if (!inFlightLikes.add(id)) return

        val wasLiked = current.isLiked
        val nowLiked = !wasLiked
        val delta: Long = if (nowLiked) 1L else -1L

        applyLocalLike(nowLiked, delta)

        viewModelScope.launch {
            try {
                val result = if (nowLiked) {
                    postRepository.postPostLike(id)
                } else {
                    postRepository.deletePostLike(id)
                }

                result.onFailure {
                    applyLocalLike(wasLiked, -delta)
                }
            } finally {
                inFlightLikes.remove(id)
            }
        }
    }

    private fun applyLocalLike(liked: Boolean, delta: Long) {
        _uiState.update { state ->
            val success = state.data as? UiState.Success ?: return@update state
            val cur = success.data
            val updated = cur.copy(
                isLiked = liked,
                likeCount = (cur.likeCount + delta).coerceAtLeast(0L)
            )
            _likeSelected.value = liked
            state.copy(data = UiState.Success(updated))
        }
    }

    fun sendComment(comment: String) {
        val text = comment.trim()
        if (text.isBlank()) return

        val current = (_uiState.value.data as? UiState.Success)?.data ?: return
        val postId = current.postId

        val body = SendComment(
            content = text
        )

        viewModelScope.launch {
            runCatching {
                postRepository.sendComment(postId = postId, content = body)
            }.onSuccess {
                load(postId)
            }.onFailure { e ->
                Log.e("PostViewModel", "sendComment error", e)
            }
        }
    }

    fun deletePost() {
        val current = (_uiState.value.data as? UiState.Success)?.data ?: return
        val postId = current.postId

        viewModelScope.launch {
            runCatching {
                postRepository.deletePost(postId = postId)
            }.onSuccess { result ->
                result.onSuccess {
                }.onFailure { e ->
                    Log.e("PostViewModel", "deletePost failure", e)
                }
            }.onFailure { e ->
                Log.e("PostViewModel", "deletePost error", e)
            }
        }
    }

    fun reportPost(reason : String) {
        val current = (_uiState.value.data as? UiState.Success)?.data ?: return
        val postId = current.postId

        val reasonRequest = ReportPostReason(
            reason = reason
        )

        viewModelScope.launch {
            runCatching {
                postRepository.reportPost(postId = postId, reason = reasonRequest)
            }.onSuccess { result ->
                result.onSuccess {
                }.onFailure { e ->
                    Log.e("PostViewModel", "deletePost failure", e)
                }
            }.onFailure { e ->
                Log.e("PostViewModel", "deletePost error", e)
            }
        }
    }

}
