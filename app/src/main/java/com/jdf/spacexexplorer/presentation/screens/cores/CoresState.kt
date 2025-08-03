package com.jdf.spacexexplorer.presentation.screens.cores

import com.jdf.spacexexplorer.domain.model.Core

/**
 * UI state for the Cores screen
 */
data class CoresState(
    val isLoading: Boolean = false,
    val cores: List<Core> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
) {
    val isInitialLoading: Boolean get() = isLoading && cores.isEmpty()
    val hasContent: Boolean get() = cores.isNotEmpty()
    val hasError: Boolean get() = error != null
    val isInLoadingState: Boolean get() = isLoading || isRefreshing
} 