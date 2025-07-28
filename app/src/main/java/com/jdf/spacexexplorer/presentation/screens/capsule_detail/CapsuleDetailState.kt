package com.jdf.spacexexplorer.presentation.screens.capsule_detail

import com.jdf.spacexexplorer.domain.model.Capsule

/**
 * UI state for the Capsule Detail screen
 */
data class CapsuleDetailState(
    val isLoading: Boolean = false,
    val capsule: Capsule? = null,
    val error: String? = null
) {
    val isInitialLoading: Boolean get() = isLoading && capsule == null
    val hasContent: Boolean get() = capsule != null
    val hasError: Boolean get() = error != null
    val isInLoadingState: Boolean get() = isLoading
} 