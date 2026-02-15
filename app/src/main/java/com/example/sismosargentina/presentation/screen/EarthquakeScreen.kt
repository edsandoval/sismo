package com.example.sismosargentina.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sismosargentina.R
import com.example.sismosargentina.presentation.components.EarthquakeCard
import com.example.sismosargentina.presentation.viewmodel.EarthquakeUiState
import com.example.sismosargentina.presentation.viewmodel.EarthquakeViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarthquakeScreen(
    viewModel: EarthquakeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.startAutoRefresh()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopAutoRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is EarthquakeUiState.Loading -> {
                    LoadingView()
                }
                is EarthquakeUiState.Success -> {
                    SuccessView(
                        earthquakes = state.earthquakes,
                        lastUpdated = state.lastUpdated,
                        onRefresh = { viewModel.loadEarthquakes() }
                    )
                }
                is EarthquakeUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.loadEarthquakes() }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun SuccessView(
    earthquakes: List<com.example.sismosargentina.data.model.Earthquake>,
    lastUpdated: Long,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Last updated info
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(
                text = "${stringResource(R.string.last_updated)}: ${formatTime(lastUpdated)}",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        if (earthquakes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_earthquakes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = earthquakes,
                    key = { it.id }
                ) { earthquake ->
                    EarthquakeCard(earthquake = earthquake)
                }
            }
        }
    }
}

@Composable
private fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = stringResource(R.string.error_loading),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return format.format(date)
}

@Preview(showBackground = true)
@Composable
fun EarthquakeScreenPreview() {
    val viewModel = getViewModel<EarthquakeViewModel>()

    MaterialTheme {
        EarthquakeScreen(
            viewModel = viewModel
        )
    }
}
