package com.umcspot.spot.landing

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
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
    fun startSocialLogin(type: SocialLoginType) = viewModelScope.launch {
        lastSocialLoginType = type   // 🔹 어떤 소셜인지 기억해 둠

        if(type == SocialLoginType.KAKAO) {
            try {
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "로그인 실패", error)
                    } else if (token != null) {
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                    }
                }
            } catch (e: Exception) {

            }
        }
    }
}
