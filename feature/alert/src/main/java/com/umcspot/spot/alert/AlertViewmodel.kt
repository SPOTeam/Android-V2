package com.umcspot.spot.alert

import androidx.lifecycle.ViewModel
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.model.AlertKind
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.umcspot.spot.designsystem.R

class AlertViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(sampleUiState())
    val uiState: StateFlow<AlertUiState> = _uiState

    fun onClickAppliedStudyCard() {
    }

    fun onClickAlert(item: AlertResult) {
        _uiState.update { state ->
            state.copy(
                alerts = state.alerts.map {
                    if (it.id == item.id) it.copy(isRead = true) else it
                }
            )
        }
    }

    companion object {
        private fun sampleUiState(): AlertUiState {
            return AlertUiState(
                showAppliedStudyCard = true,
                alerts = buildList {
                    // 🔥 POPULAR_POST: 스터디 이미지 불필요+
                    add(
                        AlertResult(
                            id = 1,
                            kind = AlertKind.POPULAR_POST,
                            title = "실시간 인기 글",
                            subtitle = "Sample Post Title",
                            isRead = false,
                        )
                    )
                    // 📢 STUDY_NOTICE: 스터디 이미지 필요 → 리소스 채움(null이면 기본으로 대체)
                    add(
                        AlertResult(
                            id = 2,
                            kind = AlertKind.STUDY_NOTICE,
                            title = "내 스터디 '공지' 업데이트",
                            subtitle = "\"Sample Study\"의 새로운 공지",
                            studyImageRes = R.drawable.sample, // 임시 썸네일
                            isRead = false
                        )
                    )
                    // 📅 STUDY_SCHEDULE
                    add(
                        AlertResult(
                            id = 3,
                            kind = AlertKind.STUDY_SCHEDULE,
                            title = "내 스터디 '새 일정' 등록",
                            subtitle = "\"Sample Study\"의 새로운 일정",
                            studyImageRes = R.drawable.sample,
                            isRead = false
                        )
                    )
                    // ✅ TODO_DONE
                    add(
                        AlertResult(
                            id = 4,
                            kind = AlertKind.TODO_DONE,
                            title = "'사용자'님의 \"Sample Todolist …\" 할 일 완료!",
                            subtitle = "\"Sample Study\"의 '사용자'님",
                            studyImageRes = R.drawable.sample,
                            isRead = false
                        )
                    )
                    // 또 하나의 인기글
                    add(
                        AlertResult(
                            id = 5,
                            kind = AlertKind.POPULAR_POST,
                            title = "실시간 인기 글",
                            subtitle = "Another Popular Post"
                        )
                    )
                    add(
                        AlertResult(
                            id = 6,
                            kind = AlertKind.POPULAR_POST,
                            title = "실시간 인기 글",
                            subtitle = "Sample Post Title",
                            isRead = false,
                        )
                    )
                    // 📢 STUDY_NOTICE: 스터디 이미지 필요 → 리소스 채움(null이면 기본으로 대체)
                    add(
                        AlertResult(
                            id = 7,
                            kind = AlertKind.STUDY_NOTICE,
                            title = "내 스터디 '공지' 업데이트",
                            subtitle = "\"Sample Study\"의 새로운 공지",
                            studyImageRes = R.drawable.sample, // 임시 썸네일
                            isRead = false
                        )
                    )
                    // 📅 STUDY_SCHEDULE
                    add(
                        AlertResult(
                            id = 8,
                            kind = AlertKind.STUDY_SCHEDULE,
                            title = "내 스터디 '새 일정' 등록",
                            subtitle = "\"Sample Study\"의 새로운 일정",
                            studyImageRes = R.drawable.sample,
                            isRead = false
                        )
                    )
                    // ✅ TODO_DONE
                    add(
                        AlertResult(
                            id = 9,
                            kind = AlertKind.TODO_DONE,
                            title = "'사용자'님의 \"Sample Todolist …\" 할 일 완료!",
                            subtitle = "\"Sample Study\"의 '사용자'님",
                            studyImageRes = R.drawable.sample,
                            isRead = false
                        )
                    )
                    // 또 하나의 인기글
                    add(
                        AlertResult(
                            id = 10,
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