package com.jdf.spacexexplorer.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Launches : Screen("launches_screen")
    object LaunchDetail : Screen("launch_detail_screen/{launchId}") {
        fun createRoute(launchId: String) = "launch_detail_screen/$launchId"
    }
    // Add more screens as needed
} 