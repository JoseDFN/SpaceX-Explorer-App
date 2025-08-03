package com.jdf.spacexexplorer.presentation.screens.launches

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdf.spacexexplorer.presentation.components.LaunchCard
import com.jdf.spacexexplorer.presentation.components.LoadingIndicator
import com.jdf.spacexexplorer.presentation.components.ErrorMessage
import com.jdf.spacexexplorer.presentation.navigation.NavigationEvent

/**
 * Main screen composable for displaying the launches list.
 */
@Composable
fun LaunchesScreen(
    navController: NavController,
    viewModel: LaunchesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

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

    when {
        state.isLoading -> LoadingIndicator()
        state.error != null -> ErrorMessage(message = state.error ?: "Unknown error")
        else -> LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.launches) { launch ->
                LaunchCard(
                    launch = launch,
                    modifier = Modifier.clickable {
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