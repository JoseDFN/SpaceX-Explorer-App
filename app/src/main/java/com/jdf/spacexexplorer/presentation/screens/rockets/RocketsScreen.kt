package com.jdf.spacexexplorer.presentation.screens.rockets

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdf.spacexexplorer.presentation.components.*
import com.jdf.spacexexplorer.presentation.navigation.NavigationEvent

/**
 * Main screen composable for displaying the rockets list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketsScreen(
    navController: NavController,
    viewModel: RocketsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Collect navigation events from ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToRocketDetail -> {
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
                        text = "Rockets",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onEvent(RocketsEvent.Refresh) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
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
                        onRetry = { viewModel.onEvent(RocketsEvent.Retry) }
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.rockets) { rocket ->
                        RocketCard(
                            rocket = rocket,
                            onClick = {
                                viewModel.onEvent(RocketsEvent.RocketClicked(rocket))
                            }
                        )
                    }
                }
            }
        }
    }
} 