package com.umcspot.spot.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.model.UserResult
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

    data class SignupUiState(val user: UiState<String> = UiState.Empty)

    private val _name = MutableStateFlow(SignupUiState())
    val name: StateFlow<SignupUiState> = _name.asStateFlow()

    fun load() {
        _name.update { it.copy(user = UiState.Loading) }

        viewModelScope.launch {
            val result = userRepository.getUserName()

            val newState = result.fold(
                onSuccess = { userResult ->
                    UiState.Success(userResult.name)  // ✅ 성공 시 이름 문자열 전달
                },
                onFailure = { e ->
                    UiState.Failure(e.message ?: e.toString())  // ✅ 실패 시 에러 메시지
                }
            )

            _name.update { it.copy(user = newState) }
        }
    }

    fun setName(newName: String) {
        _name.update { it.copy(user = UiState.Success(newName)) } // 사용하는 상태 구조에 맞게 반영
    }
}