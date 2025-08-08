package com.jdf.spacexexplorer.presentation.screens.capsules

import com.jdf.spacexexplorer.domain.model.Capsule
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * UI events for the Capsules screen
 */
sealed class CapsulesEvent {
    object Refresh : CapsulesEvent()
    data class CapsuleClicked(val capsule: Capsule) : CapsulesEvent()
    object Retry : CapsulesEvent()
    object DismissError : CapsulesEvent()
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : CapsulesEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : CapsulesEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : CapsulesEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : CapsulesEvent()
} 