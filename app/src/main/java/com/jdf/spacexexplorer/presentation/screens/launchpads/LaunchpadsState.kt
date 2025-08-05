package com.jdf.spacexexplorer.presentation.screens.launchpads

import com.jdf.spacexexplorer.domain.model.Launchpad

/**
 * UI state for the Launchpads screen.
 * This data class holds all the information needed to render the screen.
 */
data class LaunchpadsState(
    val isLoading: Boolean = false,
    val launchpads: List<Launchpad> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
) {
    /**
     * Check if the screen is in loading state (initial load)
     */
    val isInitialLoading: Boolean
        get() = isLoading && launchpads.isEmpty()
    
    /**
     * Check if the screen has content to display
     */
    val hasContent: Boolean
        get() = launchpads.isNotEmpty()
    
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