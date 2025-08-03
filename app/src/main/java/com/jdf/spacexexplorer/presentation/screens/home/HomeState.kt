package com.jdf.spacexexplorer.presentation.screens.home

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.Capsule
import com.jdf.spacexexplorer.domain.model.Core
import com.jdf.spacexexplorer.domain.model.CrewMember
import com.jdf.spacexexplorer.domain.model.Ship
import com.jdf.spacexexplorer.domain.model.Dragon

/**
 * UI state for the Home screen dashboard.
 * This data class holds all the information needed to render the dashboard with different sections.
 */
data class HomeState(
    // Unified launches section
    val launches: List<Launch> = emptyList(),
    val isLaunchesLoading: Boolean = false,
    val launchesError: String? = null,
    
    // Rockets section
    val rockets: List<Rocket> = emptyList(),
    val isRocketsLoading: Boolean = false,
    val rocketsError: String? = null,
    
    // Capsules section
    val capsules: List<Capsule> = emptyList(),
    val isCapsulesLoading: Boolean = false,
    val capsulesError: String? = null,
    
    // Cores section
    val cores: List<Core> = emptyList(),
    val isCoresLoading: Boolean = false,
    val coresError: String? = null,
    
    // Crew section
    val crew: List<CrewMember> = emptyList(),
    val isCrewLoading: Boolean = false,
    val crewError: String? = null,
    
    // Ships section
    val ships: List<Ship> = emptyList(),
    val isShipsLoading: Boolean = false,
    val shipsError: String? = null,
    
    // Dragons section
    val dragons: List<Dragon> = emptyList(),
    val isDragonsLoading: Boolean = false,
    val dragonsError: String? = null,
    
    // Overall refresh state
    val isRefreshing: Boolean = false
) {
    /**
     * Check if the launches section is in loading state
     */
    val isLaunchesInLoadingState: Boolean
        get() = isLaunchesLoading && launches.isEmpty()
    
    /**
     * Check if the rockets section is in loading state
     */
    val isRocketsInLoadingState: Boolean
        get() = isRocketsLoading && rockets.isEmpty()
    
    /**
     * Check if the capsules section is in loading state
     */
    val isCapsulesInLoadingState: Boolean
        get() = isCapsulesLoading && capsules.isEmpty()
    
    /**
     * Check if the cores section is in loading state
     */
    val isCoresInLoadingState: Boolean
        get() = isCoresLoading && cores.isEmpty()
    
    /**
     * Check if the crew section is in loading state
     */
    val isCrewInLoadingState: Boolean
        get() = isCrewLoading && crew.isEmpty()
    
    /**
     * Check if the ships section is in loading state
     */
    val isShipsInLoadingState: Boolean
        get() = isShipsLoading && ships.isEmpty()
    
    /**
     * Check if the dragons section is in loading state
     */
    val isDragonsInLoadingState: Boolean
        get() = isDragonsLoading && dragons.isEmpty()
    
    /**
     * Check if any section has content to display
     */
    val hasAnyContent: Boolean
        get() = launches.isNotEmpty() || rockets.isNotEmpty() || capsules.isNotEmpty() || 
                cores.isNotEmpty() || crew.isNotEmpty() || ships.isNotEmpty() || dragons.isNotEmpty()
    
    /**
     * Check if any section has an error
     */
    val hasAnyError: Boolean
        get() = launchesError != null || rocketsError != null || capsulesError != null || 
                coresError != null || crewError != null || shipsError != null || dragonsError != null
    
    /**
     * Check if the screen is in any loading state
     */
    val isInAnyLoadingState: Boolean
        get() = isLaunchesLoading || isRocketsLoading || isCapsulesLoading || isCoresLoading || 
                isCrewLoading || isShipsLoading || isDragonsLoading || isRefreshing
} 