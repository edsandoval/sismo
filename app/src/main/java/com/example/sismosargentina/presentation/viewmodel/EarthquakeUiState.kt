package com.example.sismosargentina.presentation.viewmodel

import com.example.sismosargentina.data.model.Earthquake

sealed class EarthquakeUiState {
    object Loading : EarthquakeUiState()
    data class Success(
        val earthquakes: List<Earthquake>,
        val lastUpdated: Long = System.currentTimeMillis()
    ) : EarthquakeUiState()
    data class Error(val message: String) : EarthquakeUiState()
}
