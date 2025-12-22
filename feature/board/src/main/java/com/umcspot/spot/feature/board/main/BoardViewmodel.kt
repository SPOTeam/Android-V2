package com.umcspot.spot.feature.board.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.domain.board.model.postList.PostResult
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

    fun load(sortBy: SortType) {
        _uiState.update { it.copy(user = UiState.Loading) }

        viewModelScope.launch {
            runCatching {
                val recentPosts = async { boardRepository.getRecentBoard() }
                val bestPosts = async { boardRepository.getBestBoard(sortBy) }

                val recents = recentPosts.await().getOrThrow()
                val bests = bestPosts.await().getOrThrow()

                Log.d("BoardRepositoryImpl", "getRecentBoard: $recents")
                Log.d("BoardRepositoryImpl", "getBestBoard: $bests")


                BoardPayload(
                    recentBoards = recents,
                    bestBoards = bests,
                    selected = sortBy,
                )
            }.onSuccess { payload ->
                _uiState.update { it.copy(user = UiState.Success(payload)) }
            }.onFailure {
                _uiState.update { it.copy(user = UiState.Empty) }
            }
        }
    }

    fun selectSort(type: SortType) {
        val cur = (uiState.value.user as? UiState.Success<BoardPayload>)?.data
        if (cur == null) {
            load(type)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(user = UiState.Success(cur.copy(selected = type))) }

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
                .onFailure { e ->
                    Log.e("BoardViewModel", "selectSort: $e")
                }
        }
    }

    fun setPostInfo(post : PostResult){
        _selectedPost.value = post
    }
}