package com.umcspot.spot.feature.board

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.domain.board.model.post.PostResult
import com.umcspot.spot.domain.board.repository.BoardRepository
import com.umcspot.spot.model.SortType
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val boardRepository: BoardRepository // 실제 구현 주입
) : ViewModel() {

    data class BoardUiState(val user: UiState<BoardPayload> = UiState.Empty)

    private val _uiState = MutableStateFlow(BoardUiState())
    val uiState: StateFlow<BoardUiState> = _uiState.asStateFlow()

    private val _selected = MutableStateFlow<List<String>>(emptyList())
    val selected = _selected.asStateFlow()

    private val _selectedPost = MutableStateFlow<PostResult?>(null)
    val selectedPost: StateFlow<PostResult?> = _selectedPost.asStateFlow()

    /** 최초/재로딩: HomeViewModel.getDummies() 스타일 */
    fun load(sortBy: SortType) {
        // 1) 로딩으로 전환
        _uiState.update { it.copy(user = UiState.Loading) }

        // 2) 실제 호출
        viewModelScope.launch {
            runCatching {
                val recentPosts = async { boardRepository.getRecentBoard() }
                val bestPosts = async { boardRepository.getBestBoard(sortBy) }
                val filteredPosts = async { boardRepository.getFilteredPosts(size = 10) }

                val recents = recentPosts.await().getOrThrow()
                val bests = bestPosts.await().getOrThrow()
                val posts = filteredPosts.await().getOrThrow()

                Log.d("BoardRepositoryImpl", "getRecentBoard: $recents")
                Log.d("BoardRepositoryImpl", "getBestBoard: $bests")
                Log.d("BoardRepositoryImpl", "getPostsBoard: $posts")


                BoardPayload(
                    recentBoards = recents,
                    bestBoards = bests,
                    selected = sortBy,
                    posts = posts
                )
            }.onSuccess { payload ->
                _uiState.update { it.copy(user = UiState.Success(payload)) }
            }.onFailure {
                // UiState.Error 타입이 없다면 Empty로 복구
                _uiState.update { it.copy(user = UiState.Empty) }
            }
        }
    }

    /** Tag 값만 새로고침 */
    fun selectSort(type: SortType) {
        val cur = (uiState.value.user as? UiState.Success<BoardPayload>)?.data
        if (cur == null) {
            load(type) // 최초엔 전체 로드
            return
        }

        viewModelScope.launch {
            // 1) 탭 선택 즉시 반영(UX 빠르게)
            _uiState.update { it.copy(user = UiState.Success(cur.copy(selected = type))) }

            // 2) 새 정렬(랜덤 순서 포함)로 tagBoards 가져와서 반영+
            runCatching { boardRepository.getBestBoard(type).getOrThrow() }
                .onSuccess { newTagBoards ->
                    _uiState.update {
                        it.copy(user = UiState.Success(
                            cur.copy(
                                selected = type,
                                bestBoards = newTagBoards
                            )
                        ))
                    }
                }
                .onFailure {
                    // 필요 시 에러 처리(토스트/스낵바 등). 최소한 선택값은 유지됨.
                }
        }
    }

    fun setPostInfo(post : PostResult){
        _selectedPost.value = post
    }
}