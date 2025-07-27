package com.jdf.spacexexplorer.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Launches : Screen("launches_screen")
    object Rockets : Screen("rockets_screen")
    object LaunchDetail : Screen("launch_detail_screen/{launchId}") {
        fun createRoute(launchId: String) = "launch_detail_screen/$launchId"
    }
    object RocketDetail : Screen("rocket_detail_screen/{rocketId}") {
        fun createRoute(rocketId: String) = "rocket_detail_screen/$rocketId"
    }
    // Add more screens as needed
} 