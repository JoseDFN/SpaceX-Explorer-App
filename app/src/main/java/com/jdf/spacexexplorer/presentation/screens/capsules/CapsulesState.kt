package com.jdf.spacexexplorer.presentation.screens.capsules

import com.jdf.spacexexplorer.domain.model.Capsule
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * UI state for the Capsules screen
 */
data class CapsulesState(
    val isLoading: Boolean = false,
    val capsules: List<Capsule> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    // Generic filter and sort management
    val availableFilters: List<FilterOption> = emptyList(),
    val activeFilters: Map<String, FilterOption> = emptyMap(),
    val currentSort: SortOption = SortOption.NAME_ASC
) {
    val isInitialLoading: Boolean get() = isLoading && capsules.isEmpty()
    val hasContent: Boolean get() = capsules.isNotEmpty()
    val hasError: Boolean get() = error != null
    val isInLoadingState: Boolean get() = isLoading || isRefreshing
    
    /**
     * Check if any filters are currently active
     */
    val hasActiveFilters: Boolean
        get() = activeFilters.isNotEmpty()
    
    /**
     * Get the list of active filters for use in API calls
     */
    val activeFiltersList: List<FilterOption>
        get() = activeFilters.values.toList()
} 