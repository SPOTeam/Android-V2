package com.umcspot.spot.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.alert.repository.AlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alertRepository: AlertRepository
) : ViewModel() {

    private val _hasUnreadAlert = MutableStateFlow(false)
    val hasUnreadAlert: StateFlow<Boolean> = _hasUnreadAlert

    private var loading = false

    fun loadUnreadAlerts() {
        if (loading) return
        loading = true

        viewModelScope.launch {
            alertRepository.getUnReadAlerts()
                .onSuccess { _hasUnreadAlert.value = it }
            loading = false
        }
    }
}
