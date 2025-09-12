package com.example.core.data.alert

import com.example.core.data.global.AlertKind

data class AlertItem(
    val id: Int,
    val kind: AlertKind,
    val title: String,
    val subtitle: String,
    val studyImageRes: Int? = null,
    val hasBlueBadge: Boolean = false
)


data class AlertUiState(
    val showAppliedStudyCard: Boolean = false,
    val alerts: List<AlertItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)