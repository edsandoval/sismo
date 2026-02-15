package com.example.sismosargentina.domain.usecase

import com.example.sismosargentina.data.model.Earthquake
import com.example.sismosargentina.data.repository.EarthquakeRepository
import kotlinx.coroutines.flow.Flow

class GetLatestEarthquakesUseCase(
    private val repository: EarthquakeRepository
) {
    operator fun invoke(limit: Int = 5): Flow<Result<List<Earthquake>>> {
        return repository.getArgentinaEarthquakes(limit)
    }
}
