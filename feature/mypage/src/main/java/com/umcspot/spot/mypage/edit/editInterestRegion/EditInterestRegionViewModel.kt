package com.umcspot.spot.mypage.edit.editInterestRegion

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.common.location.LocationStore
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditInterestRegionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<LocationRow>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<LocationRow>>> = _uiState.asStateFlow()

    private val _searchResults = MutableStateFlow<List<LocationRow>>(emptyList())
    val searchResults: StateFlow<List<LocationRow>> = _searchResults.asStateFlow()

    private var allLocations = listOf<LocationRow>()

    init {
        viewModelScope.launch {
            allLocations = LocationStore.load(appContext)
            loadPreferRegions()
        }
    }

    fun loadPreferRegions() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            userRepository.getUserPreferredRegion().onSuccess { result ->
                val rows = result.regionCodes.mapNotNull { code ->
                    allLocations.find { it.code == code }
                }
                _uiState.value = if (rows.isEmpty()) UiState.Empty else UiState.Success(rows)
            }.onFailure {
                _uiState.value = UiState.Failure(it.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        _searchResults.value = allLocations.filter {
            it.fullName.contains(query, ignoreCase = true)
        }
    }

    fun updatePreferRegions(newRegions: List<LocationRow>) {
        viewModelScope.launch {
            val codes = newRegions.map { it.code }
            userRepository.setUserPreferredRegion(codes)
                .onSuccess {
                    _uiState.value = if (newRegions.isEmpty()) UiState.Empty else UiState.Success(newRegions)
                    Log.d("EditRegion", "Update Success")
                }
                .onFailure {
                    Log.e("EditRegion", "Update Failed: ${it.message}")
                }
        }
    }
}