package com.jdf.spacexexplorer.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.Satellite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jdf.spacexexplorer.presentation.screens.home.HomeScreen
import com.jdf.spacexexplorer.presentation.navigation.Screen

/**
 * Main app shell with hamburger menu navigation drawer.
 * Contains the navigation drawer and main content area.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
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
                        // Show refresh button only on Home screen
                        if (navController.currentDestination?.route == Screen.Home.route) {
                            IconButton(
                                onClick = {
                                    // TODO: Trigger refresh for the current screen
                                    // This will be implemented when we add screen-specific actions
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
        Screen.Launches.route -> "Launches"
        Screen.Rockets.route -> "Rockets"
        Screen.Capsules.route -> "Capsules"
        Screen.Cores.route -> "Cores"
        Screen.Crew.route -> "Crew"
        Screen.LaunchDetail.route -> "Launch Details"
        Screen.RocketDetail.route -> "Rocket Details"
        Screen.CapsuleDetail.route -> "Capsule Details"
        Screen.CoreDetail.route -> "Core Details"
        Screen.CrewDetail.route -> "Crew Member Details"
        else -> "SpaceX Explorer"
    }
} 