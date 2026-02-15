package com.example.sismosargentina.data.repository

import com.example.sismosargentina.data.model.Earthquake
import com.example.sismosargentina.data.remote.EarthquakeApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class EarthquakeRepository(
    private val apiService: EarthquakeApiService
) {
    
    companion object {
        // Argentina bounding box coordinates
        private const val MIN_LATITUDE = -55.0
        private const val MAX_LATITUDE = -21.0
        private const val MIN_LONGITUDE = -73.0
        private const val MAX_LONGITUDE = -53.0
        private const val MIN_MAGNITUDE = 0.0
    }

    fun getArgentinaEarthquakes(limit: Int = 5): Flow<Result<List<Earthquake>>> = flow {
        try {
            val endTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            val startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                add(Calendar.DAY_OF_MONTH, -30) // Last 30 days
            }
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            
            val response = apiService.getEarthquakes(
                startTime = dateFormat.format(startTime.time),
                endTime = dateFormat.format(endTime.time),
                minLatitude = MIN_LATITUDE,
                maxLatitude = MAX_LATITUDE,
                minLongitude = MIN_LONGITUDE,
                maxLongitude = MAX_LONGITUDE,
                minMagnitude = MIN_MAGNITUDE,
                orderBy = "time-asc"
            )
            
            val earthquakes = response.features
                .mapNotNull { Earthquake.fromFeature(it) }
                .sortedByDescending { it.time }
                .take(limit)
            
            emit(Result.success(earthquakes))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
