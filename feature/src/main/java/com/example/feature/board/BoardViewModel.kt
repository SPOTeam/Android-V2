// BoardViewModel.kt
package com.example.feature.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.board.BoardItem
import com.example.core.data.board.BoardUiState
import com.example.core.data.board.LabeledItem
import com.example.core.data.global.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BoardViewModel @Inject constructor(
    // TODO: Repository 주입 시 여기에 넣기
) : ViewModel() {

    private val _uiState = MutableStateFlow(BoardUiState(isLoading = true))
    val uiState: StateFlow<BoardUiState> = _uiState

    init {
        load()
    }

    /** 초기/재로딩 */
    fun load() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            // TODO: 실제 API 호출로 교체
            delay(250)

            val hot = List(5) { i ->
                BoardItem(
                    id = "hot$i",
                    title = "Lorem ipsum dolor sit amet consectetur…",
                    count = listOf(10, 100, 9999, 10, 10)[i]
                )
            }
            val partners = listOf(
                LabeledItem("p1", "합격후기", "Lorem ipsum dolor sit amet consectetur…", 10),
                LabeledItem("p2", "정보공유", "Lorem ipsum dolor sit amet consectetur…", 100),
                LabeledItem("p3", "고민상담", "Lorem ipsum dolor sit amet consectetur…", 9999),
                LabeledItem("p4", "취준토크", "Lorem ipsum dolor sit amet consectetur…", 10),
                LabeledItem("p5", "자유토크", "Lorem ipsum dolor sit amet consectetur…", 10),
            )
            val notice = List(5) { i ->
                BoardItem(
                    id = "n$i",
                    title = "Lorem ipsum dolor sit amet consectetur…",
                    count = listOf(10, 100, 1100, 10, 10)[i]
                )
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    hot = hot,
                    partners = partners,
                    notice = notice
                )
            }
        } catch (t: Throwable) {
            _uiState.update { it.copy(isLoading = false, error = t.message ?: "알 수 없는 오류") }
        }
    }

    /** 탭 선택 변경 */
    fun selectSort(type: SortType) {
        _uiState.update { it.copy(selected = type) }
        // 필요하면 type에 따라 재정렬/재요청
        // e.g. refreshFor(type)
    }

    /** 예: 실시간 섹션만 새로고침 */
    fun refreshHot() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            delay(150)
            val refreshed = _uiState.value.hot.shuffled() // 샘플: 순서 랜덤
            _uiState.update { it.copy(isLoading = false, hot = refreshed) }
        } catch (t: Throwable) {
            _uiState.update { it.copy(isLoading = false, error = "새로고침 실패") }
        }
    }
}
