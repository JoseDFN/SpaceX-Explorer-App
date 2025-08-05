package com.jdf.spacexexplorer.presentation.screens.landpads

import com.jdf.spacexexplorer.domain.model.Landpad

/**
 * UI state for the Landpads screen.
 * This data class holds all the information needed to render the screen.
 */
data class LandpadsState(
    val isLoading: Boolean = false,
    val landpads: List<Landpad> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
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
} 