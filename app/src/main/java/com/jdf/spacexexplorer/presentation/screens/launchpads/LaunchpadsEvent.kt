package com.jdf.spacexexplorer.presentation.screens.launchpads

import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Launchpad
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * Sealed class representing UI events for the Launchpads screen.
 * These events are triggered by user interactions and handled by the ViewModel.
 */
sealed class LaunchpadsEvent {
    /**
     * User requested to refresh the launchpads data
     */
    object Refresh : LaunchpadsEvent()
    
    /**
     * User requested to retry loading after an error
     */
    object Retry : LaunchpadsEvent()
    
    /**
     * User dismissed an error message
     */
    object DismissError : LaunchpadsEvent()
    
    /**
     * User clicked on a specific launchpad
     */
    data class LaunchpadClicked(val launchpad: Launchpad) : LaunchpadsEvent()
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : LaunchpadsEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : LaunchpadsEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : LaunchpadsEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : LaunchpadsEvent()
} 