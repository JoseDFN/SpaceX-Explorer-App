package com.jdf.spacexexplorer.presentation.screens.core_detail

import com.jdf.spacexexplorer.domain.model.Core

/**
 * UI state for the Core Detail screen
 */
data class CoreDetailState(
    val isLoading: Boolean = false,
    val core: Core? = null,
    val error: String? = null
) {
    val isInitialLoading: Boolean get() = isLoading && core == null
    val hasContent: Boolean get() = core != null
    val hasError: Boolean get() = error != null
    val isInLoadingState: Boolean get() = isLoading
} 