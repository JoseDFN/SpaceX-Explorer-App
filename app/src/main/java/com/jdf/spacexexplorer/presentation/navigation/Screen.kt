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
    object CrewDetail : Screen("crew_detail_screen/{crewId}") {
        fun createRoute(crewId: String) = "crew_detail_screen/$crewId"
    }
    object Ships : Screen("ships_screen")
    object ShipDetail : Screen("ship_detail_screen/{shipId}") {
        fun createRoute(shipId: String) = "ship_detail_screen/$shipId"
    }
    object Dragons : Screen("dragons_screen")
    object DragonDetail : Screen("dragon_detail_screen/{dragonId}") {
        fun createRoute(dragonId: String) = "dragon_detail_screen/$dragonId"
    }
    object Landpads : Screen("landpads_screen")
    object LandpadDetail : Screen("landpad_detail_screen/{landpadId}") {
        fun createRoute(landpadId: String) = "landpad_detail_screen/$landpadId"
    }
    object Launchpads : Screen("launchpads_screen")
    object LaunchpadDetail : Screen("launchpad_detail_screen/{launchpadId}") {
        fun createRoute(launchpadId: String) = "launchpad_detail_screen/$launchpadId"
    }
    // Add more screens as needed
} 