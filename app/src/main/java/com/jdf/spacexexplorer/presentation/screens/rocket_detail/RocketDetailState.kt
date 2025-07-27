package com.jdf.spacexexplorer.presentation.screens.rocket_detail

import com.jdf.spacexexplorer.domain.model.Rocket

/**
 * UI state for the Rocket Detail screen
 */
data class RocketDetailState(
    val isLoading: Boolean = false,
    val rocket: Rocket? = null,
    val error: String? = null
) {
    val isInitialLoading: Boolean get() = isLoading && rocket == null
    val hasContent: Boolean get() = rocket != null
    val hasError: Boolean get() = error != null
    val isInLoadingState: Boolean get() = isLoading
} 