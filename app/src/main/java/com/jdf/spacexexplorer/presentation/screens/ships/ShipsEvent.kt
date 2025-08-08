package com.jdf.spacexexplorer.presentation.screens.ships

import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Ship
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * Sealed class representing UI events for the Ships screen.
 * These events are triggered by user interactions and handled by the ViewModel.
 */
sealed class ShipsEvent {
    /**
     * User requested to refresh the ships data
     */
    object Refresh : ShipsEvent()
    
    /**
     * User requested to retry loading after an error
     */
    object Retry : ShipsEvent()
    
    /**
     * User dismissed an error message
     */
    object DismissError : ShipsEvent()
    
    /**
     * User clicked on a specific ship
     */
    data class ShipClicked(val ship: Ship) : ShipsEvent()
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : ShipsEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : ShipsEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : ShipsEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : ShipsEvent()
} 