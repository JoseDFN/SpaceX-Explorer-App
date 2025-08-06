package com.jdf.spacexexplorer.presentation.screens.crew

import com.jdf.spacexexplorer.domain.model.CrewMember
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * UI state for the crew screen
 */
data class CrewState(
    val crew: List<CrewMember> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    // Generic filter and sort management
    val availableFilters: List<FilterOption> = emptyList(),
    val activeFilters: Map<String, FilterOption> = emptyMap(),
    val currentSort: SortOption = SortOption.NAME_ASC
) {
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