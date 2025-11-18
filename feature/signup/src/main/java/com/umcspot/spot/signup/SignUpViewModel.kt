package com.umcspot.spot.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    data class SignupUiState(
        val user: UiState<String> = UiState.Empty,
        val originalName: String? = null,  // 서버에서 가져온 원래 이름
        val currentName: String? = null    // 현재 수정 중인 이름
    )
    private val _name = MutableStateFlow(SignupUiState())
    val name: StateFlow<SignupUiState> = _name.asStateFlow()

    fun load() {
        _name.update { it.copy(user = UiState.Loading) }

        viewModelScope.launch {
            val result = userRepository.getUserName()

            val newState = result.fold(
                onSuccess = { userResult ->
                    UiState.Success(userResult.name)
                },
                onFailure = { e ->
                    UiState.Failure(e.message ?: e.toString())
                }
            )

            _name.update {
                it.copy(
                    user = newState,
                    originalName = (newState as? UiState.Success)?.data,
                    currentName = (newState as? UiState.Success)?.data
                )
            }
        }
    }
    fun setName(newName: String) {
        _name.update {
            it.copy(
                user = UiState.Success(newName),
                currentName = newName
            )
        }
    }

    fun saveNameIfChanged() {
        val (original, current) = getNamePair()

        if (current.isNullOrBlank() || current == original) return

        viewModelScope.launch {
            userRepository.setUserName(current)
                .onSuccess {
                    Log.d("ChangeName", "이름 변경 성공")
                    // 서버에도 저장됐으니, 로컬 state도 맞춰주기
                    _name.update {
                        it.copy(
                            user = UiState.Success(current),
                            originalName = current,
                            currentName = current
                        )
                    }
                }
                .onFailure { e ->
                    Log.e("ChangeName", "이름 변경 실패", e)
                }
        }
    }


    fun getNamePair(): Pair<String?, String?> {
        Log.d("NamePair", "${_name.value.originalName} ::: ${_name.value.currentName}")
        return _name.value.originalName to _name.value.currentName
    }
}