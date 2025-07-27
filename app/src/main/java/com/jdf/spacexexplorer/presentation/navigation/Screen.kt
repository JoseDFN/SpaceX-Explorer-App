package com.jdf.spacexexplorer.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Launches : Screen("launches_screen")
    // Add more screens as needed
} 