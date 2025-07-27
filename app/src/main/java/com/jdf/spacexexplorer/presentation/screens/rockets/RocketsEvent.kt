package com.jdf.spacexexplorer.presentation.screens.rockets

import com.jdf.spacexexplorer.domain.model.Rocket

/**
 * Sealed class representing all possible UI events for the Rockets screen
 */
sealed class RocketsEvent {
    /**
     * User requested to refresh data
     */
    object Refresh : RocketsEvent()
    
    /**
     * User clicked on a rocket item
     */
    data class RocketClicked(val rocket: Rocket) : RocketsEvent()
    
    /**
     * User requested to retry loading data
     */
    object Retry : RocketsEvent()
    
    /**
     * User dismissed an error
     */
    object DismissError : RocketsEvent()
} 