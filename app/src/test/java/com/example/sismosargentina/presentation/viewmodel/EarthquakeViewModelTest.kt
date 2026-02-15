package com.example.sismosargentina.presentation.viewmodel

import app.cash.turbine.test
import com.example.sismosargentina.data.model.Earthquake
import com.example.sismosargentina.domain.usecase.GetLatestEarthquakesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EarthquakeViewModelTest {

    private lateinit var useCase: GetLatestEarthquakesUseCase
    private lateinit var viewModel: EarthquakeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        // Given
        val mockEarthquakes = listOf(
            createMockEarthquake("1", 4.5)
        )
        every { useCase(any()) } returns flowOf(Result.success(mockEarthquakes))

        // When
        viewModel = EarthquakeViewModel(useCase)

        // Then
        assertTrue(viewModel.uiState.value is EarthquakeUiState.Loading)
    }

    @Test
    fun `loadEarthquakes updates state to success`() = runTest {
        // Given
        val mockEarthquakes = listOf(
            createMockEarthquake("1", 4.5),
            createMockEarthquake("2", 3.2)
        )
        every { useCase(any()) } returns flowOf(Result.success(mockEarthquakes))

        viewModel = EarthquakeViewModel(useCase)

        // When
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is EarthquakeUiState.Success)
        assertEquals(2, (state as EarthquakeUiState.Success).earthquakes.size)
    }

    @Test
    fun `loadEarthquakes updates state to error on failure`() = runTest {
        // Given
        val exception = Exception("Network error")
        every { useCase(any()) } returns flowOf(Result.failure(exception))

        viewModel = EarthquakeViewModel(useCase)

        // When
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is EarthquakeUiState.Error)
        assertEquals("Network error", (state as EarthquakeUiState.Error).message)
    }

    private fun createMockEarthquake(id: String, magnitude: Double) = Earthquake(
        id = id,
        magnitude = magnitude,
        place = "Test Place",
        latitude = -31.5,
        longitude = -68.5,
        depth = 10.0,
        time = System.currentTimeMillis(),
        url = null
    )
}
