package com.umcspot.spot.alert

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.alert.model.AlertInfo
import com.umcspot.spot.alert.model.AlertResult
import com.umcspot.spot.alert.repository.AlertRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertViewModel @Inject constructor(
    private val alertRepository: AlertRepository
) : ViewModel() {

    data class AlertState(
        val alerts: UiState<AlertResult> = UiState.Loading,
    )

    private val _uiState = MutableStateFlow(AlertState())
    val uiState: StateFlow<AlertState> = _uiState.asStateFlow()

    fun load() {
        _uiState.update { state -> state.copy(alerts = UiState.Loading) }

        viewModelScope.launch {
            alertRepository.getAlerts()
                .onSuccess { result: AlertResult ->
                    val newState: UiState<AlertResult> =
                        if (result.studies.isEmpty()) UiState.Empty else UiState.Success(result)

                    _uiState.update { state -> state.copy(alerts = newState) }
                }
                .onFailure { e ->
                    Log.e("AlertViewModel", "loadAlert error", e)
//                  _uiState.update { state -> state.copy(alerts = UiState.Failure(e.message ?: e.toString())) }
                }
        }
    }
}
