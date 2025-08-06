package com.jdf.spacexexplorer.presentation.screens.landpads

import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Landpad
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * UI state for the Landpads screen.
 * This data class holds all the information needed to render the screen.
 */
data class LandpadsState(
    val isLoading: Boolean = false,
    val landpads: List<Landpad> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    // Generic filter and sort management
    val availableFilters: List<FilterOption> = emptyList(),
    val activeFilters: Map<String, FilterOption> = emptyMap(),
    val currentSort: SortOption = SortOption.NAME_ASC
) {
    /**
     * Check if the screen is in loading state (initial load)
     */
    val isInitialLoading: Boolean
        get() = isLoading && landpads.isEmpty()
    
    /**
     * Check if the screen has content to display
     */
    val hasContent: Boolean
        get() = landpads.isNotEmpty()
    
    /**
     * Check if there's an error to display
     */
    val hasError: Boolean
        get() = error != null
    
    /**
     * Check if the screen is in a loading state (either initial or refresh)
     */
    val isInLoadingState: Boolean
        get() = isLoading || isRefreshing
    
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