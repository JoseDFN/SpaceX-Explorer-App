package com.jdf.spacexexplorer.presentation.screens.launches

/**
 * Sealed class representing UI events for the Launches screen.
 * These events are triggered by user interactions and handled by the ViewModel.
 */
sealed class LaunchesEvent {
    /**
     * User requested to refresh the launches data
     */
    object Refresh : LaunchesEvent()
    
    /**
     * User requested to retry loading after an error
     */
    object Retry : LaunchesEvent()
    
    /**
     * User dismissed an error message
     */
    object DismissError : LaunchesEvent()
    
    /**
     * User clicked on a specific launch
     */
    data class LaunchClicked(val launchId: String) : LaunchesEvent()
    
    /**
     * User scrolled to the bottom of the list (for pagination)
     */
    object LoadMore : LaunchesEvent()
} 