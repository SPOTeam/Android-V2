package com.umcspot.spot.post.content

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.feature.board.boardList.BoardListViewModel.BoardUiState
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import com.umcspot.spot.post.model.postDetail.PostDetailResult
import com.umcspot.spot.post.repository.PostRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun load(postId : Long) {
        _uiState.update { it.copy(data = UiState.Loading) }

        viewModelScope.launch {
            runCatching {
                // cursor 는 처음엔 null, size 는 원하는 만큼
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
}