package com.jdf.spacexexplorer.presentation.screens.ships

import com.jdf.spacexexplorer.domain.model.Ship

/**
 * UI state for the ships list screen
 */
data class ShipsState(
    val ships: List<Ship> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 