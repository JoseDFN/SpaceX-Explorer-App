package com.jdf.spacexexplorer.presentation.screens.launches

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdf.spacexexplorer.presentation.components.LaunchCard
import com.jdf.spacexexplorer.presentation.components.LoadingIndicator
import com.jdf.spacexexplorer.presentation.components.ErrorMessage
import com.jdf.spacexexplorer.presentation.navigation.NavigationEvent
import com.jdf.spacexexplorer.presentation.shared.SharedViewModel

/**
 * Main screen composable for displaying the launches list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchesScreen(
    navController: NavController,
    viewModel: LaunchesViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    // Register refresh handler with SharedViewModel
    DisposableEffect(Unit) {
        sharedViewModel.registerRefreshHandler {
            viewModel.onEvent(LaunchesEvent.Refresh)
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
                is NavigationEvent.NavigateToLaunchDetail -> {
                    navController.navigate(event.route)
                }
                else -> {
                    // Handle other navigation events if needed
                }
            }
        }
    }

    // Detect when user scrolls near the end to trigger pagination
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = state.launches.size
            val threshold = 3 // Load more when 3 items away from the end
            
            lastVisibleItem >= totalItems - threshold && 
            !state.isLoadingMore && 
            !state.endReached && 
            totalItems > 0
        }
    }

    // Trigger load more when shouldLoadMore becomes true
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.onEvent(LaunchesEvent.LoadMore)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Launches",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorMessage(message = state.error ?: "Unknown error")
                }
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.launches) { launch ->
                        LaunchCard(
                            launch = launch,
                            onClick = {
                                viewModel.onEvent(LaunchesEvent.LaunchClicked(launch.id))
                            }
                        )
                    }
                    
                    // Show loading indicator at the bottom when loading more
                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
} 