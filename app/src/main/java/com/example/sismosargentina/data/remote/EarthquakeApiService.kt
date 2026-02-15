package com.example.sismosargentina.data.remote

import com.example.sismosargentina.data.model.EarthquakeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface EarthquakeApiService {
    
    /**
     * Fetches earthquakes from USGS API
     * 
     * @param format Response format (geojson)
     * @param startTime Start time in ISO8601 format
     * @param endTime End time in ISO8601 format
     * @param minLatitude Minimum latitude for bounding box
     * @param maxLatitude Maximum latitude for bounding box
     * @param minLongitude Minimum longitude for bounding box
     * @param maxLongitude Maximum longitude for bounding box
     * @param minMagnitude Minimum magnitude filter
     * @param orderBy Order results by time
     * @param limit Maximum number of results
     */
    @GET("query")
    suspend fun getEarthquakes(
        @Query("format") format: String = "geojson",
        @Query("starttime") startTime: String? = null,
        @Query("endtime") endTime: String? = null,
        @Query("minlatitude") minLatitude: Double? = null,
        @Query("maxlatitude") maxLatitude: Double? = null,
        @Query("minlongitude") minLongitude: Double? = null,
        @Query("maxlongitude") maxLongitude: Double? = null,
        @Query("minmagnitude") minMagnitude: Double? = null,
        @Query("orderby") orderBy: String = "time-asc",
        @Query("limit") limit: Int? = null
    ): EarthquakeResponse
}
