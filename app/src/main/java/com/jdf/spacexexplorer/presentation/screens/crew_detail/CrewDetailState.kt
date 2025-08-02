package com.jdf.spacexexplorer.presentation.screens.crew_detail

import com.jdf.spacexexplorer.domain.model.CrewMember

/**
 * UI state for the crew detail screen
 */
data class CrewDetailState(
    val crewMember: CrewMember? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) 