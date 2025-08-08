package com.jdf.spacexexplorer.presentation.screens.capsules

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdf.spacexexplorer.presentation.components.*
import com.jdf.spacexexplorer.presentation.navigation.NavigationEvent
import com.jdf.spacexexplorer.presentation.shared.SharedViewModel

/**
 * Main screen composable for displaying the capsules list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapsulesScreen(
    navController: NavController,
    viewModel: CapsulesViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Register refresh handler with SharedViewModel
    DisposableEffect(Unit) {
        sharedViewModel.registerRefreshHandler {
            viewModel.onEvent(CapsulesEvent.Refresh)
        }
        
        // Clean up when the screen is disposed
        onDispose {
            sharedViewModel.clearRefreshHandler()
        }
    }

    // Collect navigation events from ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToCapsuleDetail -> {
                    navController.navigate(event.route)
                }
                else -> {
                    // Handle other navigation events if needed
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Capsules",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isInitialLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
            state.hasError -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorMessage(
                        message = state.error ?: "Unknown error occurred",
                        onRetry = { viewModel.onEvent(CapsulesEvent.Retry) }
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Generic Filter Bar
                    FilterBar(
                        filters = state.availableFilters,
                        activeFilters = state.activeFilters,
                        onEvent = viewModel::onFilterEvent,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    
                    // Capsules List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.capsules) { capsule ->
                            CapsuleCard(
                                capsule = capsule,
                                onClick = {
                                    viewModel.onEvent(CapsulesEvent.CapsuleClicked(capsule))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
} 