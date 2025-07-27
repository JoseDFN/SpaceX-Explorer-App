package com.jdf.spacexexplorer.presentation.screens.launches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Composable screen for displaying launches.
 * This is a basic implementation that demonstrates the presentation logic.
 */
@Composable
fun LaunchesScreen(
    viewModel: LaunchesViewModel = hiltViewModel(),
    onLaunchClick: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    
    LaunchesContent(
        state = state,
        onEvent = viewModel::onEvent,
        onLaunchClick = onLaunchClick
    )
}

@Composable
private fun LaunchesContent(
    state: LaunchesState,
    onEvent: (LaunchesEvent) -> Unit,
    onLaunchClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isInitialLoading -> {
                // Show loading indicator for initial load
                LoadingContent()
            }
            state.hasError -> {
                // Show error state
                ErrorContent(
                    error = state.error ?: "Unknown error",
                    onRetry = { onEvent(LaunchesEvent.Retry) },
                    onDismiss = { onEvent(LaunchesEvent.DismissError) }
                )
            }
            state.hasContent -> {
                // Show launches list
                LaunchesList(
                    launches = state.launches,
                    isLoading = state.isInLoadingState,
                    onLaunchClick = onLaunchClick,
                    onRefresh = { onEvent(LaunchesEvent.Refresh) }
                )
            }
            else -> {
                // Show empty state
                EmptyContent()
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
        TextButton(onClick = onDismiss) {
            Text("Dismiss")
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No launches available",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun LaunchesList(
    launches: List<com.jdf.spacexexplorer.domain.model.Launch>,
    isLoading: Boolean,
    onLaunchClick: (String) -> Unit,
    onRefresh: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(launches) { launch ->
            LaunchItem(
                launch = launch,
                onClick = { onLaunchClick(launch.id) }
            )
        }
    }
}

@Composable
private fun LaunchItem(
    launch: com.jdf.spacexexplorer.domain.model.Launch,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = launch.missionName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Flight #${launch.flightNumber}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = launch.launchDate,
                style = MaterialTheme.typography.bodySmall
            )
            if (launch.wasSuccessful != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (launch.wasSuccessful) "✅ Successful" else "❌ Failed",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
} 