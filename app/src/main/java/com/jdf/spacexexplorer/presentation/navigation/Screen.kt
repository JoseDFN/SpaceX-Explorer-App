package com.jdf.spacexexplorer.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Launches : Screen("launches_screen")
    object Rockets : Screen("rockets_screen")
    object Capsules : Screen("capsules_screen")
    object Cores : Screen("cores_screen")
    object LaunchDetail : Screen("launch_detail_screen/{launchId}") {
        fun createRoute(launchId: String) = "launch_detail_screen/$launchId"
    }
    object RocketDetail : Screen("rocket_detail_screen/{rocketId}") {
        fun createRoute(rocketId: String) = "rocket_detail_screen/$rocketId"
    }
    object CapsuleDetail : Screen("capsule_detail_screen/{capsuleId}") {
        fun createRoute(capsuleId: String) = "capsule_detail_screen/$capsuleId"
    }
    object CoreDetail : Screen("core_detail_screen/{coreId}") {
        fun createRoute(coreId: String) = "core_detail_screen/$coreId"
    }
    object Crew : Screen("crew_screen")
    // Add more screens as needed
} 