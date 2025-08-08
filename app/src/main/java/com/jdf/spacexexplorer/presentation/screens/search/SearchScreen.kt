package com.jdf.spacexexplorer.presentation.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdf.spacexexplorer.domain.model.SearchResult
import com.jdf.spacexexplorer.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search TextField
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            label = { Text("Search SpaceX data...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Results section
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                val errorMessage = state.error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            state.searchQuery.isBlank() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Enter a search term to find SpaceX data",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            state.results.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No results found for '${state.searchQuery}'",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                Text(
                    text = "${state.results.size} result${if (state.results.size != 1) "s" else ""} found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.results) { result ->
                        when (result) {
                            is SearchResult.LaunchResult -> {
                                LaunchCard(
                                    launch = result.launch,
                                    onClick = {
                                        navController.navigate("launch_detail_screen/${result.launch.id}")
                                    }
                                )
                            }
                            is SearchResult.RocketResult -> {
                                RocketCard(
                                    rocket = result.rocket,
                                    onClick = {
                                        navController.navigate("rocket_detail_screen/${result.rocket.id}")
                                    }
                                )
                            }
                            is SearchResult.CapsuleResult -> {
                                CapsuleCard(
                                    capsule = result.capsule,
                                    onClick = {
                                        navController.navigate("capsule_detail_screen/${result.capsule.id}")
                                    }
                                )
                            }
                            is SearchResult.CoreResult -> {
                                CoreCard(
                                    core = result.core,
                                    onClick = {
                                        navController.navigate("core_detail_screen/${result.core.id}")
                                    }
                                )
                            }
                            is SearchResult.CrewResult -> {
                                CrewCard(
                                    crewMember = result.crewMember,
                                    onClick = {
                                        navController.navigate("crew_detail_screen/${result.crewMember.id}")
                                    }
                                )
                            }
                            is SearchResult.ShipResult -> {
                                ShipCard(
                                    ship = result.ship,
                                    onClick = {
                                        navController.navigate("ship_detail_screen/${result.ship.id}")
                                    }
                                )
                            }
                            is SearchResult.DragonResult -> {
                                DragonCard(
                                    dragon = result.dragon,
                                    onClick = {
                                        navController.navigate("dragon_detail_screen/${result.dragon.id}")
                                    }
                                )
                            }
                            is SearchResult.LandpadResult -> {
                                LandpadCard(
                                    landpad = result.landpad,
                                    onClick = {
                                        navController.navigate("landpad_detail_screen/${result.landpad.id}")
                                    }
                                )
                            }
                            is SearchResult.LaunchpadResult -> {
                                LaunchpadCard(
                                    launchpad = result.launchpad,
                                    onClick = {
                                        navController.navigate("launchpad_detail_screen/${result.launchpad.id}")
                                    }
                                )
                            }
                            is SearchResult.PayloadResult -> {
                                PayloadCard(
                                    payload = result.payload,
                                    onClick = {
                                        navController.navigate("payload_detail_screen/${result.payload.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
