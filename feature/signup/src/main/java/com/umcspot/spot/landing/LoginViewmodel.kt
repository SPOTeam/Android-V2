package com.umcspot.spot.landing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

        try {
            val res = loginRepository.getRedirectUrl(type)
            val url = res.getOrNull()

            if (res.isSuccess && !url.isNullOrBlank()) {

                // Custom Tabs 오픈
                CustomTabsIntent.Builder().build().apply {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.launchUrl(context, Uri.parse(url))

            }
        } catch (e: Exception) {

        }
    }

    fun onSocialDeepLink(uri: Uri) = viewModelScope.launch {
        val code = uri.getQueryParameter("code")

        if (!code.isNullOrBlank()) {
            val tokenResponse = loginRepository.getCallBackToken(lastSocialLoginType!!, code)
            tokenResponse.onSuccess { tokens ->

                Log.d("AccessToken" , tokens.accessToken)
                Log.d("RefreshToken" , tokens.refreshToken)
            }.onFailure {

            }

        } else {

        }
    }
}
