package com.jdf.spacexexplorer.presentation.screens.crew

import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * Events that can be triggered in the crew screen
 */
sealed class CrewEvent {
    object LoadCrew : CrewEvent()
    object RefreshCrew : CrewEvent()
    object DismissError : CrewEvent()
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : CrewEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : CrewEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : CrewEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : CrewEvent()
} 