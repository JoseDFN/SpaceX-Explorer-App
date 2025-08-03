package com.jdf.spacexexplorer.presentation.screens.dragon_detail

import com.jdf.spacexexplorer.domain.model.Dragon

/**
 * UI state for the Dragon Detail screen
 */
data class DragonDetailState(
    val isLoading: Boolean = false,
    val dragon: Dragon? = null,
    val error: String? = null
) {
    val isInitialLoading: Boolean get() = isLoading && dragon == null
    val hasContent: Boolean get() = dragon != null
    val hasError: Boolean get() = error != null
    val isInLoadingState: Boolean get() = isLoading
} 