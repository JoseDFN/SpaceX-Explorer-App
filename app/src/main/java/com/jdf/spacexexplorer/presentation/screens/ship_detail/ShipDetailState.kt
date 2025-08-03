package com.jdf.spacexexplorer.presentation.screens.ship_detail

import com.jdf.spacexexplorer.domain.model.Ship

/**
 * UI state for the ship detail screen
 */
data class ShipDetailState(
    val ship: Ship? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) 