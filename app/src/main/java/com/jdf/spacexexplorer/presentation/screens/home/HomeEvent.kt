package com.jdf.spacexexplorer.presentation.screens.home

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.Capsule
import com.jdf.spacexexplorer.domain.model.Core
import com.jdf.spacexexplorer.domain.model.CrewMember
import com.jdf.spacexexplorer.domain.model.Ship
import com.jdf.spacexexplorer.domain.model.Dragon

/**
 * Sealed class representing all possible UI events for the Home screen
 */
sealed class HomeEvent {
    /**
     * User requested to refresh all data
     */
    object Refresh : HomeEvent()
    
    /**
     * User clicked on a launch item
     */
    data class LaunchClicked(val launch: Launch) : HomeEvent()
    
    /**
     * User clicked on a rocket item
     */
    data class RocketClicked(val rocket: Rocket) : HomeEvent()
    
    /**
     * User clicked on a capsule item
     */
    data class CapsuleClicked(val capsule: Capsule) : HomeEvent()
    
    /**
     * User clicked on a core item
     */
    data class CoreClicked(val core: Core) : HomeEvent()
    
    /**
     * User clicked on a crew member item
     */
    data class CrewMemberClicked(val crewMember: CrewMember) : HomeEvent()
    
    /**
     * User clicked on a ship item
     */
    data class ShipClicked(val ship: Ship) : HomeEvent()
    
    /**
     * User clicked on a dragon item
     */
    data class DragonClicked(val dragon: Dragon) : HomeEvent()
    
    /**
     * User requested to retry loading data
     */
    object Retry : HomeEvent()
    
    /**
     * User dismissed an error
     */
    object DismissError : HomeEvent()
} 