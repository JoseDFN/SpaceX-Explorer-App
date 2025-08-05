package com.jdf.spacexexplorer.presentation.screens.landpad_detail

import com.jdf.spacexexplorer.domain.model.Landpad

/**
 * UI state for the Landpad Detail screen
 */
data class LandpadDetailState(
    val isLoading: Boolean = false,
    val landpad: Landpad? = null,
    val error: String? = null
) {
    val isInitialLoading: Boolean get() = isLoading && landpad == null
    val hasContent: Boolean get() = landpad != null
    val hasError: Boolean get() = error != null
    val isInLoadingState: Boolean get() = isLoading
} 