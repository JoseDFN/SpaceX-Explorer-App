package com.jdf.spacexexplorer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jdf.spacexexplorer.presentation.screens.launches.LaunchesScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Launches.route
    ) {
        composable(route = Screen.Launches.route) {
            LaunchesScreen()
        }
        // Add more composable destinations as needed
    }
} 