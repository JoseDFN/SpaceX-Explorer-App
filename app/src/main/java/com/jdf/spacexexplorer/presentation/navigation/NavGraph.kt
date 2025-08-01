package com.jdf.spacexexplorer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jdf.spacexexplorer.presentation.screens.home.HomeScreen
import com.jdf.spacexexplorer.presentation.screens.launches.LaunchesScreen
import com.jdf.spacexexplorer.presentation.screens.launch_detail.LaunchDetailScreen
import com.jdf.spacexexplorer.presentation.screens.rockets.RocketsScreen
import com.jdf.spacexexplorer.presentation.screens.rocket_detail.RocketDetailScreen
import com.jdf.spacexexplorer.presentation.screens.capsules.CapsulesScreen
import com.jdf.spacexexplorer.presentation.screens.capsule_detail.CapsuleDetailScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(route = Screen.Launches.route) {
            LaunchesScreen(navController = navController)
        }
        
        composable(route = Screen.Rockets.route) {
            RocketsScreen(navController = navController)
        }
        
        composable(route = Screen.Capsules.route) {
            CapsulesScreen(navController = navController)
        }
        
        composable(
            route = Screen.LaunchDetail.route,
            arguments = listOf(
                navArgument("launchId") {
                    type = NavType.StringType
                }
            )
        ) { _ ->
            LaunchDetailScreen()
        }
        
        composable(
            route = Screen.RocketDetail.route,
            arguments = listOf(
                navArgument("rocketId") {
                    type = NavType.StringType
                }
            )
        ) { _ ->
            RocketDetailScreen(navController = navController)
        }
        
        composable(
            route = Screen.CapsuleDetail.route,
            arguments = listOf(
                navArgument("capsuleId") {
                    type = NavType.StringType
                }
            )
        ) { _ ->
            CapsuleDetailScreen(navController = navController)
        }
        
        // Add more composable destinations as needed
    }
} 