package com.jdf.spacexexplorer.presentation.screens.rockets

import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.SortOption

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
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : RocketsEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : RocketsEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : RocketsEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : RocketsEvent()
} 