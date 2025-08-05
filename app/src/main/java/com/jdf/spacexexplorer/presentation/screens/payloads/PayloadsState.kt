package com.jdf.spacexexplorer.presentation.screens.payloads

import com.jdf.spacexexplorer.domain.model.Payload

/**
 * UI state for the Payloads screen.
 * This data class holds all the information needed to render the screen.
 */
data class PayloadsState(
    val isLoading: Boolean = false,
    val payloads: List<Payload> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
) {
    /**
     * Check if the screen is in loading state (initial load)
     */
    val isInitialLoading: Boolean
        get() = isLoading && payloads.isEmpty()
    
    /**
     * Check if the screen has content to display
     */
    val hasContent: Boolean
        get() = payloads.isNotEmpty()
    
    /**
     * Check if there's an error to display
     */
    val hasError: Boolean
        get() = error != null
    
    /**
     * Check if the screen is in a loading state (either initial or refresh)
     */
    val isInLoadingState: Boolean
        get() = isLoading || isRefreshing
} 