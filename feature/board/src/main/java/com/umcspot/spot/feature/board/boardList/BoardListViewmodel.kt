package com.umcspot.spot.feature.board.boardList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.domain.board.model.postList.PostResult
import com.umcspot.spot.domain.board.model.postList.PostResultList
import com.umcspot.spot.domain.board.repository.BoardRepository
import com.umcspot.spot.model.PostType
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardListViewModel @Inject constructor(
    private val boardRepository: BoardRepository
) : ViewModel() {

    data class ScrollPosition(
        val index: Int = 0,
        val offset: Int = 0
    )
    data class BoardUiState(
        val data: UiState<PostResultList> = UiState.Empty
    )

    var scrollPosition: ScrollPosition = ScrollPosition()

    private val _uiState = MutableStateFlow(BoardUiState())
    val uiState: StateFlow<BoardUiState> = _uiState.asStateFlow()

    private val _selected = MutableStateFlow<List<String>>(emptyList())

    private val _selectedPost = MutableStateFlow<PostResult?>(null)

    private var currentPostType: PostType? = null
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()


    private val inFlightLikes = mutableSetOf<Long>()

    fun load(postType: PostType? = null) {
        currentPostType = postType
        _uiState.update { it.copy(data = UiState.Loading) }

        viewModelScope.launch {
            runCatching {
                boardRepository.getFilteredPosts(
                    cursor = null,
                    postType = postType,
                    size = 50
                ).getOrThrow()
            }.onSuccess { firstPage ->
                Log.d("BoardListViewModel", "load posts: $firstPage")
                Log.d("BoardListViewModel", "dataformat on First Index: ${firstPage.posts[0].createdAt}")
                _uiState.update { it.copy(data = UiState.Success(firstPage)) }
            }.onFailure { e ->
                Log.e("BoardListViewModel", "load error", e)
                _uiState.update { it.copy(data = UiState.Empty) }
            }
        }
    }

    fun loadNextPage() {
        val currentUi = _uiState.value.data
        val success = currentUi as? UiState.Success ?: return
        val currentList = success.data

        if (!currentList.hasNext) return
        if (_isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            runCatching {
                boardRepository.getFilteredPosts(
                    cursor = currentList.nextCursor,
                    postType = currentPostType,
                    size = 20
                ).getOrThrow()
            }.onSuccess { newPage ->
                val merged = currentList.copy(
                    posts = currentList.posts + newPage.posts,
                    hasNext = newPage.hasNext,
                    nextCursor = newPage.nextCursor
                )
                _uiState.update { it.copy(data = UiState.Success(merged)) }
            }.onFailure { e ->
                Log.e("BoardListViewModel", "loadNextPage error", e)
            }
            _isLoadingMore.value = false
        }
    }

    fun selectType(type: PostType?) {
        _selected.value = listOfNotNull(type?.name)
        load(type)
    }

    fun setPostInfo(post: PostResult) {
        _selectedPost.value = post
    }

    fun toggleLike(postResult: PostResult) {
        val current = (_uiState.value.data as? UiState.Success)?.data ?: return
        val target = current.posts.firstOrNull { it.postId == postResult.postId } ?: return

        val id = postResult.postId
        if (!inFlightLikes.add(id)) return

        val wasLiked = target.isLiked
        val nowLiked = !wasLiked
        val delta: Long = if (nowLiked) +1L else -1L

        applyLocalLike(id, nowLiked, delta)

        viewModelScope.launch {
            try {
                val result = if (nowLiked) {
                    boardRepository.postPostLike(id)
                } else {
                    boardRepository.deletePostLike(id)
                }

                result.onFailure {
                    applyLocalLike(id, wasLiked, -delta)
                }
            } finally {
                inFlightLikes.remove(id)
            }
        }
    }

    private fun applyLocalLike(postId: Long, liked: Boolean, delta: Long) {
        _uiState.update { state ->
            val success = state.data as? UiState.Success ?: return@update state
            val list = success.data
            val updatedPosts = list.posts.map { p ->
                if (p.postId == postId) {
                    p.copy(
                        isLiked = liked,
                        likeNum = (p.likeNum + delta).coerceAtLeast(0L)
                    )
                } else p
            }
            state.copy(data = UiState.Success(list.copy(posts = updatedPosts)))
        }

        _selectedPost.update { p ->
            if (p?.postId == postId) {
                p.copy(
                    isLiked = liked,
                    likeNum = (p.likeNum + delta).coerceAtLeast(0L)
                )
            } else p
        }
    }

    fun saveScrollPosition(index: Int, offset: Int) {
        scrollPosition = ScrollPosition(index, offset)
    }
}
