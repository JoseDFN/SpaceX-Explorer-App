package com.jdf.spacexexplorer.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
                is NavigationEvent.NavigateToCapsuleDetail -> {
                    navController.navigate(event.route)
                }
                is NavigationEvent.NavigateToCoreDetail -> {
                    navController.navigate(event.route)
                }
                is NavigationEvent.NavigateToCrewDetail -> {
                    navController.navigate(event.route)
                }
                is NavigationEvent.NavigateToShipDetail -> {
                    navController.navigate(event.route)
                }
                is NavigationEvent.NavigateToDragonDetail -> {
                    navController.navigate(event.route)
                }
                is NavigationEvent.NavigateToLandpadDetail -> {
                    navController.navigate(event.route)
                }
                is NavigationEvent.NavigateToLaunchpadDetail -> {
                    navController.navigate(event.route)
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
                           // Launches Section
               item {
                   SectionHeader(
                       title = "Launches",
                       error = state.launchesError,
                       onRetry = { viewModel.onEvent(HomeEvent.Retry) }
                   )
               }
               
               item {
                   LaunchesCarousel(
                       launches = state.launches,
                       isLoading = state.isLaunchesLoading,
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
               
               // Capsules Section
               item {
                   SectionHeader(
                       title = "Capsules",
                       error = state.capsulesError,
                       onRetry = { viewModel.onEvent(HomeEvent.Retry) }
                   )
               }
               
               item {
                   CapsulesCarousel(
                       capsules = state.capsules,
                       isLoading = state.isCapsulesLoading,
                       onCapsuleClick = { capsule ->
                           viewModel.onEvent(HomeEvent.CapsuleClicked(capsule))
                       }
                   )
               }
               
               // Cores Section
               item {
                   SectionHeader(
                       title = "Cores",
                       error = state.coresError,
                       onRetry = { viewModel.onEvent(HomeEvent.Retry) }
                   )
               }
               
               item {
                   CoresCarousel(
                       cores = state.cores,
                       isLoading = state.isCoresLoading,
                       onCoreClick = { core ->
                           viewModel.onEvent(HomeEvent.CoreClicked(core))
                       }
                   )
               }
               
               // Crew Section
               item {
                   SectionHeader(
                       title = "Crew",
                       error = state.crewError,
                       onRetry = { viewModel.onEvent(HomeEvent.Retry) }
                   )
               }
               
               item {
                   CrewCarousel(
                       crew = state.crew,
                       isLoading = state.isCrewLoading,
                       onCrewMemberClick = { crewMember ->
                           viewModel.onEvent(HomeEvent.CrewMemberClicked(crewMember))
                       }
                   )
               }
               
               // Ships Section
               item {
                   SectionHeader(
                       title = "Ships",
                       error = state.shipsError,
                       onRetry = { viewModel.onEvent(HomeEvent.Retry) }
                   )
               }
               
               item {
                   ShipsCarousel(
                       ships = state.ships,
                       isLoading = state.isShipsLoading,
                       onShipClick = { ship ->
                           viewModel.onEvent(HomeEvent.ShipClicked(ship))
                       }
                   )
               }
               
               // Dragons Section
               item {
                   SectionHeader(
                       title = "Dragons",
                       error = state.dragonsError,
                       onRetry = { viewModel.onEvent(HomeEvent.Retry) }
                   )
               }
               
               item {
                   DragonsCarousel(
                       dragons = state.dragons,
                       isLoading = state.isDragonsLoading,
                       onDragonClick = { dragon ->
                           viewModel.onEvent(HomeEvent.DragonClicked(dragon))
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
 * Carousel for launches
 */
@Composable
private fun LaunchesCarousel(
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

/**
 * Carousel for capsules
 */
@Composable
private fun CapsulesCarousel(
    capsules: List<com.jdf.spacexexplorer.domain.model.Capsule>,
    isLoading: Boolean,
    onCapsuleClick: (com.jdf.spacexexplorer.domain.model.Capsule) -> Unit
) {
    if (isLoading && capsules.isEmpty()) {
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
            items(capsules) { capsule ->
                CapsuleCarouselCard(
                    capsule = capsule,
                    onClick = { onCapsuleClick(capsule) }
                )
            }
        }
    }
}

/**
 * Carousel for cores
 */
@Composable
private fun CoresCarousel(
    cores: List<com.jdf.spacexexplorer.domain.model.Core>,
    isLoading: Boolean,
    onCoreClick: (com.jdf.spacexexplorer.domain.model.Core) -> Unit
) {
    if (isLoading && cores.isEmpty()) {
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
            items(cores) { core ->
                CoreCarouselCard(
                    core = core,
                    onClick = { onCoreClick(core) }
                )
            }
        }
    }
}

/**
 * Carousel for crew
 */
@Composable
private fun CrewCarousel(
    crew: List<com.jdf.spacexexplorer.domain.model.CrewMember>,
    isLoading: Boolean,
    onCrewMemberClick: (com.jdf.spacexexplorer.domain.model.CrewMember) -> Unit
) {
    if (isLoading && crew.isEmpty()) {
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
            items(crew) { crewMember ->
                CrewMemberCarouselCard(
                    crewMember = crewMember,
                    onClick = { onCrewMemberClick(crewMember) }
                )
            }
        }
    }
}

/**
 * Carousel for ships
 */
@Composable
private fun ShipsCarousel(
    ships: List<com.jdf.spacexexplorer.domain.model.Ship>,
    isLoading: Boolean,
    onShipClick: (com.jdf.spacexexplorer.domain.model.Ship) -> Unit
) {
    if (isLoading && ships.isEmpty()) {
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
            items(ships) { ship ->
                ShipCarouselCard(
                    ship = ship,
                    onClick = { onShipClick(ship) }
                )
            }
        }
    }
}

/**
 * Carousel for dragons
 */
@Composable
private fun DragonsCarousel(
    dragons: List<com.jdf.spacexexplorer.domain.model.Dragon>,
    isLoading: Boolean,
    onDragonClick: (com.jdf.spacexexplorer.domain.model.Dragon) -> Unit
) {
    if (isLoading && dragons.isEmpty()) {
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
            items(dragons) { dragon ->
                DragonCarouselCard(
                    dragon = dragon,
                    onClick = { onDragonClick(dragon) }
                )
            }
        }
    }
} 