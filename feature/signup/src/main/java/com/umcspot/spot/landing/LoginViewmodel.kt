package com.umcspot.spot.landing

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val loginRepository: TokenRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var lastSocialLoginType: SocialLoginType? = null

    sealed interface LoginEvent {
        data object LoginSucceeded : LoginEvent
        data class ShowError(val message: String) : LoginEvent
    }

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    /** 카카오/네이버 공용 시작 함수 */
    fun startSocialLogin(
        type: SocialLoginType,
        activity: Activity,   // ← Activity를 받기
    ) = viewModelScope.launch {
        lastSocialLoginType = type   // 🔹 어떤 소셜인지 기억해 둠

        if(type == SocialLoginType.KAKAO) {
            try {
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "로그인 실패", error)
                    } else if (token != null) {
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                        viewModelScope.launch {
                            runCatching {
                                // 보통은 accessToken만 넘김
                                loginRepository.finishSocialLogin(
                                    type = lastSocialLoginType!!,
                                    accessToken = token.accessToken
                                )
                            }.onSuccess {
                                _events.emit(LoginEvent.LoginSucceeded)
                            }.onFailure { e ->
                                Log.e(TAG, "서버 로그인 실패", e)
                                _events.emit(LoginEvent.ShowError("서버 로그인 실패: ${e.message}"))
                            }
                        }
                    }
                }
            } catch (e: Exception) {

            }
        } else if (type == SocialLoginType.NAVER) {
            val nidOAuthCallback = object : NidOAuthCallback {
                override fun onSuccess() {
                    // 네이버 SDK 내부에서 로그인 성공
                    val accessToken = NidOAuth.getAccessToken()

                    if (accessToken.isNullOrBlank()) {
                        Log.e(TAG, "네이버 로그인 성공했지만 accessToken이 null/blank")
                        viewModelScope.launch {
                            _events.emit(LoginEvent.ShowError("네이버 로그인 토큰을 가져오지 못했습니다."))
                        }
                        return
                    }

                    Log.i(TAG, "네이버 로그인 성공 accessToken = $accessToken")

                    // 카카오와 동일하게 서버 로그인 처리
                    viewModelScope.launch {
                        runCatching {
                            loginRepository.finishSocialLogin(
                                type = lastSocialLoginType!!,   // NAVER
                                accessToken = accessToken       // 네이버 access token
                            )
                        }.onSuccess {
                            _events.emit(LoginEvent.LoginSucceeded)
                        }.onFailure { e ->
                            Log.e(TAG, "네이버 서버 로그인 실패", e)
                            _events.emit(LoginEvent.ShowError("서버 로그인 실패: ${e.message}"))
                        }
                    }
                }

                override fun onFailure(errorCode: String, errorDesc: String) {
                    Log.e(TAG, "네이버 로그인 실패: $errorCode, $errorDesc")
                    viewModelScope.launch {
                        _events.emit(LoginEvent.ShowError("네이버 로그인 실패: $errorDesc"))
                    }
                }
            }

            try {
                // context: @ApplicationContext 주입받은 Context
                NidOAuth.requestLogin(activity, nidOAuthCallback)
            } catch (e: Exception) {
                Log.e(TAG, "네이버 로그인 요청 중 예외", e)
                viewModelScope.launch {
                    _events.emit(LoginEvent.ShowError("네이버 로그인 오류: ${e.message}"))
                }
            }
        }
    }
}

