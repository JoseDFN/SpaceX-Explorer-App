package com.jdf.spacexexplorer.presentation.navigation

/**
 * Sealed class representing navigation events that can be emitted by ViewModels
 * and collected by UI components to trigger navigation.
 */
sealed class NavigationEvent {
    data class NavigateToLaunchDetail(val launchId: String) : NavigationEvent() {
        val route: String = Screen.LaunchDetail.createRoute(launchId)
    }
    
    data class NavigateToRocketDetail(val rocketId: String) : NavigationEvent() {
        val route: String = Screen.RocketDetail.createRoute(rocketId)
    }
    
    data class NavigateToCapsuleDetail(val capsuleId: String) : NavigationEvent() {
        val route: String = Screen.CapsuleDetail.createRoute(capsuleId)
    }
    
    data class NavigateToCoreDetail(val coreId: String) : NavigationEvent() {
        val route: String = Screen.CoreDetail.createRoute(coreId)
    }
    
    data class NavigateToCrewDetail(val crewId: String) : NavigationEvent() {
        val route: String = Screen.CrewDetail.createRoute(crewId)
    }
    
    data class NavigateToShipDetail(val shipId: String) : NavigationEvent() {
        val route: String = Screen.ShipDetail.createRoute(shipId)
    }
    
    data class NavigateToDragonDetail(val dragonId: String) : NavigationEvent() {
        val route: String = Screen.DragonDetail.createRoute(dragonId)
    }
} 