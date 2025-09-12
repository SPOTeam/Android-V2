package com.example.feature.alert

import androidx.lifecycle.ViewModel
import com.example.core.data.alert.AlertItem
import com.example.core.data.alert.AlertUiState
import com.example.core.data.global.AlertKind
import com.example.core.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AlertViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(sampleUiState())
    val uiState: StateFlow<AlertUiState> = _uiState

    fun onClickAppliedStudyCard() {
    }

    fun onClickAlert(item: AlertItem) {
    }

    companion object {
        private fun sampleUiState(): AlertUiState {
            return AlertUiState(
                showAppliedStudyCard = true,
                alerts = buildList {
                    // 🔥 POPULAR_POST: 스터디 이미지 불필요
                    add(
                        AlertItem(
                            id = 1,
                            kind = AlertKind.POPULAR_POST,
                            title = "실시간 인기 글",
                            subtitle = "Sample Post Title",
                            hasBlueBadge = false,
                        )
                    )
                    // 📢 STUDY_NOTICE: 스터디 이미지 필요 → 리소스 채움(null이면 기본으로 대체)
                    add(
                        AlertItem(
                            id = 2,
                            kind = AlertKind.STUDY_NOTICE,
                            title = "내 스터디 '공지' 업데이트",
                            subtitle = "\"Sample Study\"의 새로운 공지",
                            studyImageRes = R.drawable.sample, // 임시 썸네일
                            hasBlueBadge = true
                        )
                    )
                    // 📅 STUDY_SCHEDULE
                    add(
                        AlertItem(
                            id = 3,
                            kind = AlertKind.STUDY_SCHEDULE,
                            title = "내 스터디 '새 일정' 등록",
                            subtitle = "\"Sample Study\"의 새로운 일정",
                            studyImageRes = R.drawable.sample,
                            hasBlueBadge = true
                        )
                    )
                    // ✅ TODO_DONE
                    add(
                        AlertItem(
                            id = 4,
                            kind = AlertKind.TODO_DONE,
                            title = "'사용자'님의 \"Sample Todolist …\" 할 일 완료!",
                            subtitle = "\"Sample Study\"의 '사용자'님",
                            studyImageRes = R.drawable.sample,
                            hasBlueBadge = true
                        )
                    )
                    // 또 하나의 인기글
                    add(
                        AlertItem(
                            id = 5,
                            kind = AlertKind.POPULAR_POST,
                            title = "실시간 인기 글",
                            subtitle = "Another Popular Post"
                        )
                    )
                }
            )
        }
    }
}
