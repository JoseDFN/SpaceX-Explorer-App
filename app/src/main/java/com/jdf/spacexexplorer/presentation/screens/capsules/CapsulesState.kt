package com.jdf.spacexexplorer.presentation.screens.capsules

import com.jdf.spacexexplorer.domain.model.Capsule

/**
 * UI state for the Capsules screen
 */
data class CapsulesState(
    val isLoading: Boolean = false,
    val capsules: List<Capsule> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
) {
    val isInitialLoading: Boolean get() = isLoading && capsules.isEmpty()
    val hasContent: Boolean get() = capsules.isNotEmpty()
    val hasError: Boolean get() = error != null
    val isInLoadingState: Boolean get() = isLoading || isRefreshing
} 