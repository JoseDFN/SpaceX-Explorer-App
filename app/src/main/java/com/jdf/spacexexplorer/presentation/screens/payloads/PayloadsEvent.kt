package com.jdf.spacexexplorer.presentation.screens.payloads

import com.jdf.spacexexplorer.domain.model.Payload

/**
 * Sealed class representing all possible UI events for the Payloads screen
 */
sealed class PayloadsEvent {
    /**
     * User requested to refresh data
     */
    object Refresh : PayloadsEvent()
    
    /**
     * User clicked on a payload item
     */
    data class PayloadClicked(val payload: Payload) : PayloadsEvent()
    
    /**
     * User requested to retry loading data
     */
    object Retry : PayloadsEvent()
    
    /**
     * User dismissed an error
     */
    object DismissError : PayloadsEvent()
} 