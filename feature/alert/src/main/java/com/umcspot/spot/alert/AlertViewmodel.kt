package com.umcspot.spot.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.alert.model.AlertInfo
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.alert.model.AppliedAlertInfo
import com.umcspot.spot.alert.model.AppliedAlertResult
import com.umcspot.spot.alert.repository.AlertRepository
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
class AlertViewModel @Inject constructor (
    private val alertRepository: AlertRepository
) : ViewModel() {

    data class AlertUiState(
        val general: UiState<AlertResult> = UiState.Empty,
        val applied: UiState<AppliedAlertResult> = UiState.Empty,
        val hasAppliedData: Boolean = false
    )
    private val _uiState = MutableStateFlow(AlertUiState())

    val uiState: StateFlow<AlertUiState> = _uiState.asStateFlow()

    fun load() {
        _uiState.update { it.copy(general = UiState.Loading, applied = UiState.Loading) }

        viewModelScope.launch {
            val generalDefer = async { alertRepository.getAlerts() }          // Result<AlertResult>
            val appliedDefer = async { alertRepository.getAppliedAlerts() }   // Result<AppliedAlertResult>

            val generalRes = generalDefer.await()
            val appliedRes = appliedDefer.await()

            // general: alerts가 비면 UiState.Empty
            _uiState.update { prev ->
                val newGeneral = generalRes.toUiState { result -> result.alerts.isEmpty() }
                prev.copy(general = newGeneral)
            }

            // applied: alerts가 비면 UiState.Empty + hasAppliedData 계산
            _uiState.update { prev ->
                val newApplied = appliedRes.toUiState { result -> result.alerts.isEmpty() }
                val hasApplied = newApplied is UiState.Success
                prev.copy(applied = newApplied, hasAppliedData = hasApplied)
            }
        }
    }


    /** 일반 알림 개별 클릭 -> 읽음 처리 */
    fun onClickAlert(item: AlertInfo) {
        val currentGeneral = (_uiState.value.general as? UiState.Success)?.data ?: return
        val updated = currentGeneral.copy(
            alerts = currentGeneral.alerts.map { if (it.id == item.id) it.copy(isRead = true) else it }
        )
        _uiState.update { it.copy(general = UiState.Success(updated)) }
    }

    fun onRejectClick(item: AppliedAlertInfo) {
        // 거절 API 호출
    }

    fun onAcceptClick(item: AppliedAlertInfo) {
        // 승인 API 호출
    }
}

// AlertViewModel 내부(클래스 바디 최하단이나 load 위)에 추가
private inline fun <T> Result<T>.toUiState(
    crossinline isEmpty: (T) -> Boolean
): UiState<T> = fold(
    onSuccess = { data -> if (isEmpty(data)) UiState.Empty else UiState.Success(data) },
    onFailure = { e -> UiState.Failure(e.message ?: e.toString()) }
)