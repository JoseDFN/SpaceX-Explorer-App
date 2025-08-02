package com.jdf.spacexexplorer.presentation.screens.crew

import com.jdf.spacexexplorer.domain.model.CrewMember

/**
 * UI state for the crew screen
 */
data class CrewState(
    val crew: List<CrewMember> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
) 