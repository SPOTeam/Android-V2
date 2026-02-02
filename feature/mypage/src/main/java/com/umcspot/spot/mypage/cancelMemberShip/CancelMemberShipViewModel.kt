package com.umcspot.spot.mypage.cancelMemberShip

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancelMemberShipViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _leaveStatus = MutableStateFlow("")
    val leaveStatus : StateFlow<String> = _leaveStatus

    fun leaveSpot() {
        Log.d("CancelMemberShipVM", "leaveSpot CALLED")
        viewModelScope.launch {
            userRepository.leaveSpot()
                .onSuccess { status ->
                    Log.d("CancelMemberShipVM", "leaveSpot success status=$status")
                    _leaveStatus.value = status
                }
                .onFailure { e ->
                    Log.e("CancelMemberShipVM", "leaveSpot failed", e)
                }
        }
    }
}

