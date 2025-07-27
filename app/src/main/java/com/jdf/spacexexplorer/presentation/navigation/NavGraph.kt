package com.jdf.spacexexplorer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jdf.spacexexplorer.presentation.screens.launches.LaunchesScreen
import com.jdf.spacexexplorer.presentation.screens.launch_detail.LaunchDetailScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Launches.route
    ) {
        composable(route = Screen.Launches.route) {
            LaunchesScreen(navController = navController)
        }
        
        composable(
            route = Screen.LaunchDetail.route,
            arguments = listOf(
                navArgument("launchId") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            LaunchDetailScreen()
        }
        
        // Add more composable destinations as needed
    }
} 