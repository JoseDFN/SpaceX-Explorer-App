package com.jdf.spacexexplorer.presentation.screens.landpads

import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Landpad
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * Sealed class representing UI events for the Landpads screen.
 * These events are triggered by user interactions and handled by the ViewModel.
 */
sealed class LandpadsEvent {
    /**
     * User requested to refresh the landpads data
     */
    object Refresh : LandpadsEvent()
    
    /**
     * User requested to retry loading after an error
     */
    object Retry : LandpadsEvent()
    
    /**
     * User dismissed an error message
     */
    object DismissError : LandpadsEvent()
    
    /**
     * User clicked on a specific landpad
     */
    data class LandpadClicked(val landpad: Landpad) : LandpadsEvent()
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : LandpadsEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : LandpadsEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : LandpadsEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : LandpadsEvent()
} 