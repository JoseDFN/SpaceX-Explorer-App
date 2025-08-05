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
import com.jdf.spacexexplorer.presentation.screens.cores.CoresScreen
import com.jdf.spacexexplorer.presentation.screens.core_detail.CoreDetailScreen
import com.jdf.spacexexplorer.presentation.screens.crew.CrewScreen
import com.jdf.spacexexplorer.presentation.screens.crew_detail.CrewDetailScreen
import com.jdf.spacexexplorer.presentation.screens.ships.ShipsScreen
import com.jdf.spacexexplorer.presentation.screens.ship_detail.ShipDetailScreen
import com.jdf.spacexexplorer.presentation.screens.dragons.DragonsScreen
import com.jdf.spacexexplorer.presentation.screens.dragon_detail.DragonDetailScreen
import com.jdf.spacexexplorer.presentation.screens.landpads.LandpadsScreen
import com.jdf.spacexexplorer.presentation.screens.landpad_detail.LandpadDetailScreen
import com.jdf.spacexexplorer.presentation.screens.launchpads.LaunchpadsScreen
import com.jdf.spacexexplorer.presentation.shared.SharedViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(route = Screen.Launches.route) {
            LaunchesScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(route = Screen.Rockets.route) {
            RocketsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(route = Screen.Capsules.route) {
            CapsulesScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(route = Screen.Cores.route) {
            CoresScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(route = Screen.Crew.route) {
            CrewScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(route = Screen.Ships.route) {
            ShipsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(route = Screen.Dragons.route) {
            DragonsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(route = Screen.Landpads.route) {
            LandpadsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(route = Screen.Launchpads.route) {
            LaunchpadsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        
        composable(
            route = Screen.ShipDetail.route,
            arguments = listOf(
                navArgument("shipId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val shipId = backStackEntry.arguments?.getString("shipId") ?: ""
            ShipDetailScreen(
                navController = navController,
                shipId = shipId
            )
        }
        
        composable(
            route = Screen.CrewDetail.route,
            arguments = listOf(
                navArgument("crewId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val crewId = backStackEntry.arguments?.getString("crewId") ?: ""
            CrewDetailScreen(
                navController = navController,
                crewId = crewId
            )
        }
        
        composable(
            route = Screen.LaunchDetail.route,
            arguments = listOf(
                navArgument("launchId") {
                    type = NavType.StringType
                }
            )
        ) { _ ->
            LaunchDetailScreen(navController = navController)
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
        
        composable(
            route = Screen.CoreDetail.route,
            arguments = listOf(
                navArgument("coreId") {
                    type = NavType.StringType
                }
            )
        ) { _ ->
            CoreDetailScreen(navController = navController)
        }
        
        composable(
            route = Screen.DragonDetail.route,
            arguments = listOf(
                navArgument("dragonId") {
                    type = NavType.StringType
                }
            )
        ) { _ ->
            DragonDetailScreen(navController = navController)
        }
        
        composable(
            route = Screen.LandpadDetail.route,
            arguments = listOf(
                navArgument("landpadId") {
                    type = NavType.StringType
                }
            )
        ) { _ ->
            LandpadDetailScreen(navController = navController)
        }
        
        // Add more composable destinations as needed
    }
} 