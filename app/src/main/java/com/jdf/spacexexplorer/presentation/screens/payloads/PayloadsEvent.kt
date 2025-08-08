package com.jdf.spacexexplorer.presentation.screens.payloads

import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Payload
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * Sealed class representing UI events for the Payloads screen.
 * These events are triggered by user interactions and handled by the ViewModel.
 */
sealed class PayloadsEvent {
    /**
     * User requested to refresh the payloads data
     */
    object Refresh : PayloadsEvent()
    
    /**
     * User requested to retry loading after an error
     */
    object Retry : PayloadsEvent()
    
    /**
     * User dismissed an error message
     */
    object DismissError : PayloadsEvent()
    
    /**
     * User clicked on a specific payload
     */
    data class PayloadClicked(val payload: Payload) : PayloadsEvent()
    
    /**
     * User updated a filter option
     */
    data class UpdateFilter(val filter: FilterOption) : PayloadsEvent()
    
    /**
     * User removed a filter option
     */
    data class RemoveFilter(val filterKey: String) : PayloadsEvent()
    
    /**
     * User cleared all active filters
     */
    object ClearAllFilters : PayloadsEvent()
    
    /**
     * User changed the sort option
     */
    data class UpdateSort(val sort: SortOption) : PayloadsEvent()
} 