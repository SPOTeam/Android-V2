package com.umcspot.spot.signup.landing

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
class LandingViewModel @Inject constructor(
    private val loginRepository: TokenRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandingState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<LandingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var autoLoginChecked = false

    fun tryAutoLogin() {
        if (autoLoginChecked) return
        autoLoginChecked = true

        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = loginRepository.refreshTokenData()
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
                _sideEffect.emit(LandingSideEffect.NavigateToHome)
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun startSocialLogin(
        type: SocialLoginType,
        activity: Activity,
    ) {
        _uiState.update { it.copy(isLoading = true) }

        when (type) {
            SocialLoginType.KAKAO -> loginWithKakao()
            SocialLoginType.NAVER -> loginWithNaver(activity)
        }
    }

    private fun loginWithKakao() = viewModelScope.launch {

        try {
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    handleLoginError("카카오 로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "카카오 로그인 성공: ${token.accessToken}")
                    requestServerLogin(SocialLoginType.KAKAO, token.accessToken)
                }
            }
        } catch (e: Exception) {
            handleLoginError("카카오 로그인 예외 발생", e)
        }
    }


    private fun loginWithNaver(activity: Activity) {
        val nidOAuthCallback = object : NidOAuthCallback {
            override fun onSuccess() {
                val accessToken = NidOAuth.getAccessToken()
                if (accessToken.isNullOrBlank()) {
                    handleLoginError("네이버 토큰이 유효하지 않습니다.")
                    return
                }
                Log.i(TAG, "네이버 로그인 성공: $accessToken")

                requestServerLogin(SocialLoginType.NAVER, accessToken)
            }

            override fun onFailure(errorCode: String, errorDesc: String) {
                handleLoginError("네이버 로그인 실패: $errorDesc")
            }
        }

        try {
            NidOAuth.requestLogin(activity, nidOAuthCallback)
        } catch (e: Exception) {
            handleLoginError("네이버 로그인 요청 중 예외", e)
        }
    }


    private fun requestServerLogin(type: SocialLoginType, accessToken: String) =
        viewModelScope.launch {
            val result = loginRepository.finishSocialLogin(type = type, accessToken = accessToken)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
                _sideEffect.emit(LandingSideEffect.NavigateToSignUp)
            } else {
                handleLoginError("서버 로그인 실패", result.exceptionOrNull())
            }
        }

    private fun handleLoginError(msg: String, e: Throwable? = null) = viewModelScope.launch {
        val errorMessage = if (e != null) "$msg: ${e.message}" else msg
        Log.e(TAG, errorMessage, e)

        _uiState.update { it.copy(isLoading = false) }
        _sideEffect.emit(LandingSideEffect.ShowSnackBar(errorMessage))
    }
}
