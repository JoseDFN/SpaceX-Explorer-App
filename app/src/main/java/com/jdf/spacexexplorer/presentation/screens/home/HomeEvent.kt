package com.jdf.spacexexplorer.presentation.screens.home

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket

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
     * User requested to retry loading data
     */
    object Retry : HomeEvent()
    
    /**
     * User dismissed an error
     */
    object DismissError : HomeEvent()
} 