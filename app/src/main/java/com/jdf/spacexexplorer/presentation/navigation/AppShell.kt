package com.jdf.spacexexplorer.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.Satellite
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.jdf.spacexexplorer.presentation.screens.home.HomeScreen
import com.jdf.spacexexplorer.presentation.navigation.Screen
import com.jdf.spacexexplorer.presentation.shared.SharedViewModel

/**
 * Main app shell with hamburger menu navigation drawer.
 * Contains the navigation drawer and main content area.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(
    navController: NavHostController = rememberNavController(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sharedState by sharedViewModel.state.collectAsState()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Drawer header
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "SpaceX Explorer",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Navigation items
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = navController.currentDestination?.route == Screen.Home.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Home.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Satellite, contentDescription = null) },
                    label = { Text("Launches") },
                    selected = navController.currentDestination?.route == Screen.Launches.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Launches.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Rocket, contentDescription = null) },
                    label = { Text("Rockets") },
                    selected = navController.currentDestination?.route == Screen.Rockets.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Rockets.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Satellite, contentDescription = null) },
                    label = { Text("Capsules") },
                    selected = navController.currentDestination?.route == Screen.Capsules.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Capsules.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Rocket, contentDescription = null) },
                    label = { Text("Cores") },
                    selected = navController.currentDestination?.route == Screen.Cores.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Cores.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Crew") },
                    selected = navController.currentDestination?.route == Screen.Crew.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Crew.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.DirectionsBoat, contentDescription = null) },
                    label = { Text("Ships") },
                    selected = navController.currentDestination?.route == Screen.Ships.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Ships.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Flight, contentDescription = null) },
                    label = { Text("Dragons") },
                    selected = navController.currentDestination?.route == Screen.Dragons.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Dragons.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    label = { Text("Landpads") },
                    selected = navController.currentDestination?.route == Screen.Landpads.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Landpads.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Rocket, contentDescription = null) },
                    label = { Text("Launchpads") },
                    selected = navController.currentDestination?.route == Screen.Launchpads.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Launchpads.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Satellite, contentDescription = null) },
                    label = { Text("Payloads") },
                    selected = navController.currentDestination?.route == Screen.Payloads.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.Payloads.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Footer
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                Text(
                    text = "Version 1.0",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) {
        // Main content
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = getScreenTitle(navController.currentDestination?.route),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                // Open the drawer
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        // Search button
                        IconButton(
                            onClick = {
                                if (navController.currentDestination?.route == Screen.Search.route) {
                                    // If already on search screen, navigate back to home
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Search.route) { inclusive = true }
                                    }
                                } else {
                                    // Navigate to search screen
                                    navController.navigate(Screen.Search.route)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                        
                        // Show refresh button when a refresh handler is available
                        if (sharedState.onRefresh != null) {
                            IconButton(
                                onClick = {
                                    sharedState.onRefresh?.invoke()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh"
                                )
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            // NavHost content
            SetupNavGraph(
                navController = navController,
                sharedViewModel = sharedViewModel,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

/**
 * Get the screen title based on the current route
 */
@Composable
private fun getScreenTitle(route: String?): String {
    return when (route) {
        Screen.Home.route -> "Home"
        Screen.Search.route -> "Search"
        Screen.Launches.route -> "Launches"
        Screen.Rockets.route -> "Rockets"
        Screen.Capsules.route -> "Capsules"
        Screen.Cores.route -> "Cores"
        Screen.Crew.route -> "Crew"
        Screen.Ships.route -> "Ships"
        Screen.Dragons.route -> "Dragons"
        Screen.Landpads.route -> "Landpads"
        Screen.Launchpads.route -> "Launchpads"
        Screen.Payloads.route -> "Payloads"
        Screen.PayloadDetail.route -> "Payload Details"
        Screen.DragonDetail.route -> "Dragon Details"
        Screen.LandpadDetail.route -> "Landpad Details"
        Screen.LaunchpadDetail.route -> "Launchpad Details"
        Screen.ShipDetail.route -> "Ship Details"
        Screen.LaunchDetail.route -> "Launch Details"
        Screen.RocketDetail.route -> "Rocket Details"
        Screen.CapsuleDetail.route -> "Capsule Details"
        Screen.CoreDetail.route -> "Core Details"
        Screen.CrewDetail.route -> "Crew Member Details"
        else -> "SpaceX Explorer"
    }
} 