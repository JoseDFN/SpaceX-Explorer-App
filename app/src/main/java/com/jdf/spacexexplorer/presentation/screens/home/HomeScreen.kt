package com.jdf.spacexexplorer.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
 * Main screen composable for the Home dashboard.
 * Displays multiple sections with horizontal carousels for launches and rockets.
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    // Register refresh handler with SharedViewModel
    DisposableEffect(Unit) {
        sharedViewModel.registerRefreshHandler {
            viewModel.onEvent(HomeEvent.Retry)
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
                is NavigationEvent.NavigateToRocketDetail -> {
                    navController.navigate(event.route)
                }
                else -> {
                    // Handle other navigation events if needed
                }
            }
        }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
            // Latest Launches Section
            item {
                SectionHeader(
                    title = "Latest Launches",
                    error = state.latestLaunchesError,
                    onRetry = { viewModel.onEvent(HomeEvent.Retry) }
                )
            }
            
            item {
                LatestLaunchesCarousel(
                    launches = state.latestLaunches,
                    isLoading = state.isLatestLaunchesLoading,
                    onLaunchClick = { launch ->
                        viewModel.onEvent(HomeEvent.LaunchClicked(launch))
                    }
                )
            }
            
            // Upcoming Launches Section
            item {
                SectionHeader(
                    title = "Upcoming Launches",
                    error = state.upcomingLaunchesError,
                    onRetry = { viewModel.onEvent(HomeEvent.Retry) }
                )
            }
            
            item {
                UpcomingLaunchesCarousel(
                    launches = state.upcomingLaunches,
                    isLoading = state.isUpcomingLaunchesLoading,
                    onLaunchClick = { launch ->
                        viewModel.onEvent(HomeEvent.LaunchClicked(launch))
                    }
                )
            }
            
            // Rockets Section
            item {
                SectionHeader(
                    title = "Rockets",
                    error = state.rocketsError,
                    onRetry = { viewModel.onEvent(HomeEvent.Retry) }
                )
            }
            
            item {
                RocketsCarousel(
                    rockets = state.rockets,
                    isLoading = state.isRocketsLoading,
                    onRocketClick = { rocket ->
                        viewModel.onEvent(HomeEvent.RocketClicked(rocket))
                    }
                )
            }
        }
    }


/**
 * Header component for each section with title, loading state, and error handling
 */
@Composable
private fun SectionHeader(
    title: String,
    error: String?,
    onRetry: () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        // Show error message if there's an error
        error?.let { errorMessage ->
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                
                TextButton(
                    onClick = onRetry,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Retry")
                }
            }
        }
    }
}

/**
 * Carousel for latest launches
 */
@Composable
private fun LatestLaunchesCarousel(
    launches: List<com.jdf.spacexexplorer.domain.model.Launch>,
    isLoading: Boolean,
    onLaunchClick: (com.jdf.spacexexplorer.domain.model.Launch) -> Unit
) {
    if (isLoading && launches.isEmpty()) {
        // Show loading skeleton
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(120.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(launches) { launch ->
                LaunchCarouselCard(
                    launch = launch,
                    onClick = { onLaunchClick(launch) }
                )
            }
        }
    }
}

/**
 * Carousel for upcoming launches
 */
@Composable
private fun UpcomingLaunchesCarousel(
    launches: List<com.jdf.spacexexplorer.domain.model.Launch>,
    isLoading: Boolean,
    onLaunchClick: (com.jdf.spacexexplorer.domain.model.Launch) -> Unit
) {
    if (isLoading && launches.isEmpty()) {
        // Show loading skeleton
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(120.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(launches) { launch ->
                LaunchCarouselCard(
                    launch = launch,
                    onClick = { onLaunchClick(launch) }
                )
            }
        }
    }
}

/**
 * Carousel for rockets
 */
@Composable
private fun RocketsCarousel(
    rockets: List<com.jdf.spacexexplorer.domain.model.Rocket>,
    isLoading: Boolean,
    onRocketClick: (com.jdf.spacexexplorer.domain.model.Rocket) -> Unit
) {
    if (isLoading && rockets.isEmpty()) {
        // Show loading skeleton
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(120.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rockets) { rocket ->
                RocketCarouselCard(
                    rocket = rocket,
                    onClick = { onRocketClick(rocket) }
                )
            }
        }
    }
} 