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
import kotlin.fold

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
            // 병렬 요청
            val generalDefer = async { alertRepository.getAlerts() }                 // Result<AlertResult>
            val appliedDefer = async { alertRepository.getAppliedAlerts() }          // Result<AppliedAlertResult>

            val generalRes = generalDefer.await()
            val appliedRes = appliedDefer.await()

            // general 반영
            _uiState.update { prev ->
                val newGeneral = generalRes.fold(
                    onSuccess = { UiState.Success(it) },
                    onFailure = { UiState.Failure(it.toString()) }
                )
                prev.copy(general = newGeneral)
            }

            // applied 반영 + 데이터 존재 여부 계산
            _uiState.update { prev ->
                val newApplied = appliedRes.fold(
                    onSuccess = { UiState.Success(it) },
                    onFailure = { UiState.Failure(it.toString()) }
                )
                val hasApplied = (newApplied as? UiState.Success)?.data?.alerts?.isNotEmpty() == true
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

    }

    fun onAcceptClick(item: AppliedAlertInfo) {

    }
}