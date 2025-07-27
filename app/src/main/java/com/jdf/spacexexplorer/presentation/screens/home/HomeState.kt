package com.jdf.spacexexplorer.presentation.screens.home

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket

/**
 * UI state for the Home screen dashboard.
 * This data class holds all the information needed to render the dashboard with different sections.
 */
data class HomeState(
    // Latest launches section
    val latestLaunches: List<Launch> = emptyList(),
    val isLatestLaunchesLoading: Boolean = false,
    val latestLaunchesError: String? = null,
    
    // Upcoming launches section
    val upcomingLaunches: List<Launch> = emptyList(),
    val isUpcomingLaunchesLoading: Boolean = false,
    val upcomingLaunchesError: String? = null,
    
    // Rockets section
    val rockets: List<Rocket> = emptyList(),
    val isRocketsLoading: Boolean = false,
    val rocketsError: String? = null,
    
    // Overall refresh state
    val isRefreshing: Boolean = false
) {
    /**
     * Check if the latest launches section is in loading state
     */
    val isLatestLaunchesInLoadingState: Boolean
        get() = isLatestLaunchesLoading && latestLaunches.isEmpty()
    
    /**
     * Check if the upcoming launches section is in loading state
     */
    val isUpcomingLaunchesInLoadingState: Boolean
        get() = isUpcomingLaunchesLoading && upcomingLaunches.isEmpty()
    
    /**
     * Check if the rockets section is in loading state
     */
    val isRocketsInLoadingState: Boolean
        get() = isRocketsLoading && rockets.isEmpty()
    
    /**
     * Check if any section has content to display
     */
    val hasAnyContent: Boolean
        get() = latestLaunches.isNotEmpty() || upcomingLaunches.isNotEmpty() || rockets.isNotEmpty()
    
    /**
     * Check if any section has an error
     */
    val hasAnyError: Boolean
        get() = latestLaunchesError != null || upcomingLaunchesError != null || rocketsError != null
    
    /**
     * Check if the screen is in any loading state
     */
    val isInAnyLoadingState: Boolean
        get() = isLatestLaunchesLoading || isUpcomingLaunchesLoading || isRocketsLoading || isRefreshing
} 