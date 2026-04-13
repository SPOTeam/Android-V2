package com.umcspot.spot.mypage

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.token.repository.TokenRepository
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(MyPageState())
    val uiState: StateFlow<MyPageState> = _uiState

    fun load() {
        loadPrefer()
        loadMemberInfo()
        loadAppVersion()
    }

    fun loadMemberInfo() {
        _uiState.update { it.copy(memberInfo = UiState.Loading) }

        viewModelScope.launch {
            userRepository.getMyPageInfo()
                .onSuccess { info ->
                    _uiState.update { it.copy(memberInfo = UiState.Success(info)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadMyPageInfo error", e)
                }
        }
    }

    fun loadPrefer() {
        _uiState.update {
            it.copy(
                preferRegions = UiState.Loading,
                preferCategories = UiState.Loading
            )
        }

        viewModelScope.launch {
            userRepository.getUserPreferredRegionName()
                .onSuccess { info ->
                    _uiState.update { it.copy(preferRegions = UiState.Success(info.regionCodes)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadPreferredRegion error", e)
                }
        }

        viewModelScope.launch {
            userRepository.getUserPreferredCategory()
                .onSuccess { info ->
                    val titles = info.categories.mapNotNull { it?.title }
                    _uiState.update { it.copy(preferCategories = UiState.Success(titles)) }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadPreferCategories error", e)
                }
        }
    }

    fun loadAppVersion() {
        _uiState.update { it.copy(appVersion = UiState.Loading) }

        try {
            val context = getApplication<Application>()
            val pm = context.packageManager
            val pkgName = context.packageName

            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(
                    pkgName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(pkgName, 0)
            }

            val versionName = packageInfo.versionName ?: "unknown"

            _uiState.update {
                it.copy(appVersion = UiState.Success(versionName))
            }
        } catch (e: Exception) {
            Log.e("MyPageViewModel", "loadAppVersion error", e)
        }
    }

    suspend fun logout() = tokenRepository.spotLogout()
}