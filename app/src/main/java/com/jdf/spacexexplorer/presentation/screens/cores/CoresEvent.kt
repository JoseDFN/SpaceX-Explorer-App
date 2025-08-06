package com.jdf.spacexexplorer.presentation.screens.cores

import com.jdf.spacexexplorer.domain.model.Core
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * UI events for the Cores screen
 */
sealed class CoresEvent {
    object Refresh : CoresEvent()
    data class CoreClicked(val core: Core) : CoresEvent()
    object Retry : CoresEvent()
    object DismissError : CoresEvent()
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : CoresEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : CoresEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : CoresEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : CoresEvent()
} 