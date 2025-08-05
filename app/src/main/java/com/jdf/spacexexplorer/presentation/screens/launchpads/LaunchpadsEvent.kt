package com.jdf.spacexexplorer.presentation.screens.launchpads

import com.jdf.spacexexplorer.domain.model.Launchpad

/**
 * Sealed class representing all possible UI events for the Launchpads screen
 */
sealed class LaunchpadsEvent {
    /**
     * User requested to refresh data
     */
    object Refresh : LaunchpadsEvent()
    
    /**
     * User clicked on a launchpad item
     */
    data class LaunchpadClicked(val launchpad: Launchpad) : LaunchpadsEvent()
    
    /**
     * User requested to retry loading data
     */
    object Retry : LaunchpadsEvent()
    
    /**
     * User dismissed an error
     */
    object DismissError : LaunchpadsEvent()
} 