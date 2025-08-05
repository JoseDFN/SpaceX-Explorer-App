package com.jdf.spacexexplorer.presentation.screens.payload_detail

import com.jdf.spacexexplorer.domain.model.Payload

/**
 * UI state for the Payload Detail screen.
 * This data class holds all the information needed to render the screen.
 */
data class PayloadDetailState(
    val isLoading: Boolean = false,
    val payload: Payload? = null,
    val error: String? = null
) {
    /**
     * Check if the screen is in loading state
     */
    val isInitialLoading: Boolean
        get() = isLoading && payload == null
    
    /**
     * Check if the screen has content to display
     */
    val hasContent: Boolean
        get() = payload != null
    
    /**
     * Check if there's an error to display
     */
    val hasError: Boolean
        get() = error != null
} 