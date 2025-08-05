package com.jdf.spacexexplorer.presentation.screens.launchpad_detail

/**
 * Sealed class representing all possible UI events for the Launchpad detail screen
 */
sealed class LaunchpadDetailEvent {
    /**
     * User requested to retry loading data
     */
    object Retry : LaunchpadDetailEvent()
    
    /**
     * User dismissed an error
     */
    object DismissError : LaunchpadDetailEvent()
} 