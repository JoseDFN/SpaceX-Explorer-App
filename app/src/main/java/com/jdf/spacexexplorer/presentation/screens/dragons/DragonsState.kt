package com.jdf.spacexexplorer.presentation.screens.dragons

import com.jdf.spacexexplorer.domain.model.Dragon

/**
 * UI state for the Dragons screen.
 * This data class holds all the information needed to render the screen.
 */
data class DragonsState(
    val isLoading: Boolean = false,
    val dragons: List<Dragon> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
) {
    /**
     * Check if the screen is in loading state (initial load)
     */
    val isInitialLoading: Boolean
        get() = isLoading && dragons.isEmpty()
    
    /**
     * Check if the screen has content to display
     */
    val hasContent: Boolean
        get() = dragons.isNotEmpty()
    
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