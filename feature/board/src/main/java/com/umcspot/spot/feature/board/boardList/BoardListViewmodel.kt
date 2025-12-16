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

    // 선택된 게시판 타입(텍스트 표시용)
    private val _selected = MutableStateFlow<List<String>>(emptyList())
    val selected: StateFlow<List<String>> = _selected.asStateFlow()

    private val _selectedPost = MutableStateFlow<PostResult?>(null)
    val selectedPost: StateFlow<PostResult?> = _selectedPost.asStateFlow()

    private var currentPostType: PostType? = null
    private var isLoadingMore: Boolean = false

    private val inFlightLikes = mutableSetOf<Long>()

    /** 최초/재로딩: postType 없이 전체 불러오기 or 특정 타입만 */
    fun load(postType: PostType? = null) {
        _uiState.update { it.copy(data = UiState.Loading) }

        viewModelScope.launch {
            runCatching {
                // cursor 는 처음엔 null, size 는 원하는 만큼
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

    /** 무한 스크롤용 다음 페이지 로딩 */
    fun loadNextPage() {
        val currentUi = _uiState.value.data
        val success = currentUi as? UiState.Success ?: return
        val currentList = success.data

        // 더 이상 불러올 게 없거나 이미 로딩 중이면 종료
        if (!currentList.hasNext || isLoadingMore) return

        viewModelScope.launch {
            isLoadingMore = true
            runCatching {
                boardRepository.getFilteredPosts(
                    cursor = currentList.nextCursor,   // ✅ hasNext == true면 nextCursor 사용
                    postType = currentPostType,
                    size = 20
                ).getOrThrow()
            }.onSuccess { newPage ->
                // 이전 리스트 + 새 리스트 append
                val merged = currentList.copy(
                    posts = currentList.posts + newPage.posts,
                    hasNext = newPage.hasNext,
                    nextCursor = newPage.nextCursor
                )
                _uiState.update { it.copy(data = UiState.Success(merged)) }

            }.onFailure { e ->
                Log.e("BoardListViewModel", "loadNextPage error", e)
                // 페이징 실패했다고 해서 전체를 Empty로 바꾸진 말고, 기존 데이터 유지도 가능
                // _uiState.update { it.copy(user = UiState.Failure(e.message ?: "error")) }
            }
            isLoadingMore = false
        }
    }

    /** 타입 선택 시: 선택 상태 업데이트 + 해당 타입으로 재요청 */
    fun selectType(type: PostType?) {
        _selected.value = listOfNotNull(type?.name)
        load(type)
    }

    /** 상세 화면 등에 사용할 선택된 게시글 저장 */
    fun setPostInfo(post: PostResult) {
        _selectedPost.value = post
    }

    fun getPostInfo() : PostResult? {
        return _selectedPost.value
    }

    fun toggleLike(postResult: PostResult) {
        val current = (_uiState.value.data as? UiState.Success)?.data ?: return
        val target = current.posts.firstOrNull { it.postId == postResult.postId } ?: return

        val id = postResult.postId
        if (!inFlightLikes.add(id)) return // 이미 진행 중이면 무시

        val wasLiked = target.isLiked
        val nowLiked = !wasLiked
        val delta: Long = if (nowLiked) +1L else -1L

        // 1) 로컬 즉시 반영
        applyLocalLike(id, nowLiked, delta)

        // 2) 네트워크
        viewModelScope.launch {
            try {
                val result = if (nowLiked) {
                    boardRepository.postPostLike(id)      // Result<Unit>
                } else {
                    boardRepository.deletePostLike(id)    // Result<Unit>
                }

                result.onFailure {
                    // 실패 시 롤백
                    applyLocalLike(id, wasLiked, -delta)
                }
            } finally {
                inFlightLikes.remove(id) // 반드시 해제
            }
        }
    }

    /** 리스트/선택된 포스트 둘 다 업데이트 */
    private fun applyLocalLike(postId: Long, liked: Boolean, delta: Long) {
        // 리스트 갱신
        _uiState.update { state ->
            val success = state.data as? UiState.Success ?: return@update state
            val list = success.data
            val updatedPosts = list.posts.map { p ->
                if (p.postId == postId) {
                    p.copy(
                        isLiked = liked,
                        likeNum = (p.likeNum + delta).coerceAtLeast(0L) // ← Long 기준
                    )
                } else p
            }
            state.copy(data = UiState.Success(list.copy(posts = updatedPosts)))
        }
        // 상세 선택 포스트도 갱신
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

    fun resetScrollPosition() {
        scrollPosition = ScrollPosition()
    }
}
