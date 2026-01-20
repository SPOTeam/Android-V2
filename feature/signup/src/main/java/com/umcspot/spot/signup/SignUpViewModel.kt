package com.umcspot.spot.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SignupSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadUserName()
    }

    private fun loadUserName() = viewModelScope.launch {
        userRepository.getUserName().fold(
            onSuccess = { userResult ->
                _uiState.update {
                    it.copy(
                        originalName = userResult.name,
                        currentName = userResult.name
                    )
                }
            },
            onFailure = { e ->
                _sideEffect.emit(SignupSideEffect.ShowSnackBar("이름을 불러오지 못했습니다."))
            }
        )
    }

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(
                currentName = newName
            )
        }
    }

    fun onPrivacyCheckChange(checked: Boolean) {
        _uiState.update { it.copy(isPrivacyChecked = checked) }
    }

    fun onUniqueCheckChange(checked: Boolean) {
        _uiState.update { it.copy(isUniqueChecked = checked) }
    }


    fun onNextClick() = viewModelScope.launch {
        val currentState = _uiState.value


        if (!currentState.isPrivacyChecked || !currentState.isUniqueChecked) {
            return@launch
        }


        if (isNameChanged()) {
            val currentName = currentState.currentName ?: return@launch
            userRepository.setUserName(currentName)
                .onSuccess {

                    _uiState.update { it.copy(originalName = currentName) }
                    _sideEffect.emit(SignupSideEffect.NavigateToCheckList)
                }
                .onFailure {
                    _sideEffect.emit(SignupSideEffect.ShowSnackBar("이름 저장 실패: ${it.message}"))
                }
        } else {

            _sideEffect.emit(SignupSideEffect.NavigateToCheckList)
        }
    }

    private fun isNameChanged(): Boolean {
        val state = _uiState.value
        return !state.currentName.isNullOrBlank() && state.currentName != state.originalName
    }

    fun saveNameIfChanged() = viewModelScope.launch {
        if (isNameChanged()) {
            val currentName = _uiState.value.currentName ?: return@launch
            userRepository.setUserName(currentName)
                .onSuccess { _uiState.update { it.copy(originalName = currentName) } }
        }
    }
}