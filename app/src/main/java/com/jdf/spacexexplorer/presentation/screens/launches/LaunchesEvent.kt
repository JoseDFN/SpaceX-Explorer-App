package com.jdf.spacexexplorer.presentation.screens.launches

import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption

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
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : LaunchesEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : LaunchesEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : LaunchesEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : LaunchesEvent()
} 