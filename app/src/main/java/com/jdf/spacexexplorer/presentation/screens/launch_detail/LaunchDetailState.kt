package com.jdf.spacexexplorer.presentation.screens.launch_detail

import com.jdf.spacexexplorer.domain.model.Launch

/**
 * UI state for the Launch Detail screen.
 * This data class holds all the information needed to render the launch detail screen.
 */
data class LaunchDetailState(
    val isLoading: Boolean = false,
    val launch: Launch? = null,
    val error: String? = null
) {
    /**
     * Check if the screen is in loading state
     */
    val isInLoadingState: Boolean
        get() = isLoading

    /**
     * Check if the screen has content to display
     */
    val hasContent: Boolean
        get() = launch != null

    /**
     * Check if there's an error to display
     */
    val hasError: Boolean
        get() = error != null

    /**
     * Check if the launch was successful
     */
    val isLaunchSuccessful: Boolean
        get() = launch?.wasSuccessful == true

    /**
     * Check if the launch is upcoming
     */
    val isLaunchUpcoming: Boolean
        get() = launch?.isUpcoming == true
} 