package com.umcspot.spot.signup.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.token.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepository: TokenRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SplashSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var hasStartedAutoLogin = false

    fun tryAutoLogin() {
        if (hasStartedAutoLogin) return
        hasStartedAutoLogin = true

        viewModelScope.launch {
            val minimumSplashDurationMs = 2000L
            val startedAt = System.currentTimeMillis()

            val result = loginRepository.refreshTokenData()

            val elapsed = System.currentTimeMillis() - startedAt
            val remain = minimumSplashDurationMs - elapsed
            if (remain > 0) delay(remain)

            if (result.isSuccess) {
                _sideEffect.emit(SplashSideEffect.NavigateToHome)
                _uiState.update { it.copy(successAutoLogin = true) }
            } else {
                _sideEffect.emit(SplashSideEffect.NavigateToLanding)
                _uiState.update { it.copy(successAutoLogin = false) }
            }
        }
    }
}
