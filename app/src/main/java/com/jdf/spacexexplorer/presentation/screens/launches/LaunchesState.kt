package com.jdf.spacexexplorer.presentation.screens.launches

import com.jdf.spacexexplorer.domain.model.Launch

/**
 * UI state for the Launches screen.
 * This data class holds all the information needed to render the screen.
 */
data class LaunchesState(
    val isLoading: Boolean = false,
    val launches: List<Launch> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false,
    val currentPage: Int = 0
) {
    /**
     * Check if the screen is in loading state (initial load)
     */
    val isInitialLoading: Boolean
        get() = isLoading && launches.isEmpty()
    
    /**
     * Check if the screen has content to display
     */
    val hasContent: Boolean
        get() = launches.isNotEmpty()
    
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
} 