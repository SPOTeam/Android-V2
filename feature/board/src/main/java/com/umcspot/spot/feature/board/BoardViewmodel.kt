package com.umcspot.spot.feature.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    /** 최초/재로딩: HomeViewModel.getDummies() 스타일 */
    fun load(selected: SortType) {
        // 1) 로딩으로 전환
        _uiState.update { it.copy(user = UiState.Loading) }

        // 2) 실제 호출
        viewModelScope.launch {
            runCatching {
                val tagDeferred = async { boardRepository.getTagBoardData(selected) }
                val rankedDeferred = async { boardRepository.getRankedBoardData() }
                val labeledDeferred = async { boardRepository.getLabeledBoardData() }

                val tagBoards = tagDeferred.await().getOrThrow()
                val rankedBoards = rankedDeferred.await().getOrThrow()
                val labeledBoards = labeledDeferred.await().getOrThrow()

                BoardPayload(
                    tagBoards = tagBoards,
                    rankedBoards = rankedBoards,
                    labeledBoards = labeledBoards,
                    selected = selected
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
            runCatching { boardRepository.getTagBoardData(type).getOrThrow() }
                .onSuccess { newTagBoards ->
                    _uiState.update {
                        it.copy(user = UiState.Success(
                            cur.copy(
                                selected = type,
                                tagBoards = newTagBoards
                            )
                        ))
                    }
                }
                .onFailure {
                    // 필요 시 에러 처리(토스트/스낵바 등). 최소한 선택값은 유지됨.
                }
        }
    }

    /** 전체 새로고침이 필요할 때 */
    fun refreshAll() {
        val sel = (uiState.value.user as? UiState.Success<BoardPayload>)?.data?.selected ?: return
        load(sel)
    }
}