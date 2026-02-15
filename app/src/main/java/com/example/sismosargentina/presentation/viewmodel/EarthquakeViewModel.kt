package com.example.sismosargentina.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sismosargentina.domain.usecase.GetLatestEarthquakesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EarthquakeViewModel(
    private val getLatestEarthquakesUseCase: GetLatestEarthquakesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EarthquakeUiState>(EarthquakeUiState.Loading)
    val uiState: StateFlow<EarthquakeUiState> = _uiState.asStateFlow()

    private var isAutoRefreshActive = false

    init {
        loadEarthquakes()
    }

    fun loadEarthquakes() {
        viewModelScope.launch {
            _uiState.value = EarthquakeUiState.Loading
            
            getLatestEarthquakesUseCase(limit = 5).collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { earthquakes ->
                        EarthquakeUiState.Success(
                            earthquakes = earthquakes,
                            lastUpdated = System.currentTimeMillis()
                        )
                    },
                    onFailure = { exception ->
                        EarthquakeUiState.Error(
                            message = exception.message ?: "Error desconocido al cargar datos"
                        )
                    }
                )
            }
        }
    }

    fun startAutoRefresh() {
        if (isAutoRefreshActive) return
        
        isAutoRefreshActive = true
        viewModelScope.launch {
            while (isAutoRefreshActive) {
                delay(120_000) // 2 minutes in milliseconds
                loadEarthquakes()
            }
        }
    }

    fun stopAutoRefresh() {
        isAutoRefreshActive = false
    }

    override fun onCleared() {
        super.onCleared()
        stopAutoRefresh()
    }
}
