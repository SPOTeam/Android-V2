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

    private val _uiState = MutableStateFlow(BoardState())
    val uiState: StateFlow<BoardState> = _uiState

    private val _sortType = MutableStateFlow(SortType.RECENT)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    fun load() {
        loadRecentBoard()
        loadBestBoard()
    }

    fun loadRecentBoard() {
        _uiState.update { it.copy(recentBoards = UiState.Loading) }

        viewModelScope.launch {
            boardRepository.getRecentBoard()
                .onSuccess { recent ->
                    _uiState.update { it.copy(recentBoards = UiState.Success(recent)) }
                }.onFailure { e ->
                    Log.e("BoardViewModel", "loadRecentBoard: $e")
                }
        }
    }

    fun loadBestBoard() {
        _uiState.update { it.copy(bestBoards = UiState.Loading) }

        viewModelScope.launch {
            boardRepository.getBestBoard(_sortType.value)
                .onSuccess { best ->
                    _uiState.update { it.copy(bestBoards = UiState.Success(best)) }
                }.onFailure { e ->
                    Log.e("BoardViewModel", "loadBestBoard: $e")
                }
        }
    }

    fun selectSort(type: SortType) {
        _sortType.value = type
        loadBestBoard()
    }
}