package com.jdf.spacexexplorer.presentation.screens.dragons

import com.jdf.spacexexplorer.domain.model.Dragon
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * Sealed class representing UI events for the Dragons screen.
 * These events are triggered by user interactions and handled by the ViewModel.
 */
sealed class DragonsEvent {
    /**
     * User requested to refresh the dragons data
     */
    object Refresh : DragonsEvent()
    
    /**
     * User requested to retry loading after an error
     */
    object Retry : DragonsEvent()
    
    /**
     * User dismissed an error message
     */
    object DismissError : DragonsEvent()
    
    /**
     * User clicked on a specific dragon
     */
    data class DragonClicked(val dragonId: String) : DragonsEvent()
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : DragonsEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : DragonsEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : DragonsEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : DragonsEvent()
} 