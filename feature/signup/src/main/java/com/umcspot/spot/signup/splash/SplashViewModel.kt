package com.umcspot.spot.signup.splash

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.oauth.util.NidOAuthCallback
import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.token.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepository: TokenRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandingState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<LandingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun tryAutoLogin() {
        _uiState.update { it.copy(successAutoLogin = true) }

        viewModelScope.launch {
            val result = loginRepository.refreshTokenData()
            if (result.isSuccess) {
                _sideEffect.emit(LandingSideEffect.NavigateToHome)
                _uiState.update { it.copy(successAutoLogin = false) }
            } else {
                _sideEffect.emit(LandingSideEffect.NavigateToLanding)
                _uiState.update { it.copy(successAutoLogin = false) }
            }
        }
    }
}
