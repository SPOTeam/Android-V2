package com.umcspot.spot.alert

import com.umcspot.spot.alert.model.AlertResult

data class AlertUiState(
    val showAppliedStudyCard: Boolean = false,
    val alerts: List<AlertResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)