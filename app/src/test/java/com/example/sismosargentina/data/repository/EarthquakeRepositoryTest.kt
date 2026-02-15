package com.example.sismosargentina.data.repository

import com.example.sismosargentina.data.model.*
import com.example.sismosargentina.data.remote.EarthquakeApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EarthquakeRepositoryTest {

    private lateinit var apiService: EarthquakeApiService
    private lateinit var repository: EarthquakeRepository

    @Before
    fun setup() {
        apiService = mockk()
        repository = EarthquakeRepository(apiService)
    }

    @Test
    fun `getArgentinaEarthquakes returns success with data`() = runTest {
        // Given
        val mockResponse = EarthquakeResponse(
            type = "FeatureCollection",
            features = listOf(
                createMockFeature(
                    id = "test1",
                    magnitude = 4.5,
                    place = "San Juan, Argentina",
                    lat = -31.5,
                    lon = -68.5,
                    depth = 10.0,
                    time = System.currentTimeMillis()
                )
            ),
            metadata = Metadata(
                generated = System.currentTimeMillis(),
                count = 1,
                status = 200,
                title = "Test"
            )
        )

        coEvery {
            apiService.getEarthquakes(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } returns mockResponse

        // When
        val result = repository.getArgentinaEarthquakes(5).first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("test1", result.getOrNull()?.first()?.id)
        assertEquals(4.5, result.getOrNull()?.first()?.magnitude, 0.01)
    }

    @Test
    fun `getArgentinaEarthquakes returns error on exception`() = runTest {
        // Given
        coEvery {
            apiService.getEarthquakes(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } throws Exception("Network error")

        // When
        val result = repository.getArgentinaEarthquakes(5).first()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    private fun createMockFeature(
        id: String,
        magnitude: Double,
        place: String,
        lat: Double,
        lon: Double,
        depth: Double,
        time: Long
    ) = EarthquakeFeature(
        type = "Feature",
        id = id,
        properties = EarthquakeProperties(
            magnitude = magnitude,
            place = place,
            time = time,
            updated = time,
            url = null,
            detail = null,
            status = "reviewed",
            type = "earthquake"
        ),
        geometry = Geometry(
            type = "Point",
            coordinates = listOf(lon, lat, depth)
        )
    )
}
