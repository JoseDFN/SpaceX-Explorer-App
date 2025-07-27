package com.jdf.spacexexplorer.presentation.screens.rockets

import com.jdf.spacexexplorer.domain.model.Rocket

/**
 * UI state for the Rockets screen.
 * This data class holds all the information needed to render the screen.
 */
data class RocketsState(
    val isLoading: Boolean = false,
    val rockets: List<Rocket> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
) {
    /**
     * Check if the screen is in loading state (initial load)
     */
    val isInitialLoading: Boolean
        get() = isLoading && rockets.isEmpty()
    
    /**
     * Check if the screen has content to display
     */
    val hasContent: Boolean
        get() = rockets.isNotEmpty()
    
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