package com.umcspot.spot.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.home.model.Dummy
import com.umcspot.spot.home.repository.HomeRepository
import com.umcspot.spot.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val dummyRepository: HomeRepository
) : ViewModel() {
    var uiState = MutableStateFlow(HomeState())
        private set

    fun getDummies(
        id: Int,
        email: String
    ) {
        viewModelScope.launch {
            dummyRepository.getDummies(
                request = Dummy(id = id, email = email)
            )
                .onSuccess { response ->
                    uiState.update {
                        it.copy(
                            user = UiState.Success(response)
                        )
                    }
                }
                .onFailure { e ->
                    uiState.update {
                        it.copy(user = UiState.Failure(e.message ?: "오류 발생"))
                    }
                }
        }
    }
}