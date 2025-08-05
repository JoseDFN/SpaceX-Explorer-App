package com.jdf.spacexexplorer.presentation.screens.launchpad_detail

import com.jdf.spacexexplorer.domain.model.Launchpad

/**
 * UI state for the Launchpad detail screen.
 * This data class holds all the information needed to render the screen.
 */
data class LaunchpadDetailState(
    val isLoading: Boolean = false,
    val launchpad: Launchpad? = null,
    val error: String? = null
) {
    /**
     * Check if the screen is in loading state
     */
    val isInitialLoading: Boolean
        get() = isLoading && launchpad == null
    
    /**
     * Check if the screen has content to display
     */
    val hasContent: Boolean
        get() = launchpad != null
    
    /**
     * Check if there's an error to display
     */
    val hasError: Boolean
        get() = error != null
} 