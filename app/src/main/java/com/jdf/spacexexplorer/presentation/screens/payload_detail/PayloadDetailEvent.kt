package com.jdf.spacexexplorer.presentation.screens.payload_detail

/**
 * Sealed class representing all possible UI events for the Payload Detail screen
 */
sealed class PayloadDetailEvent {
    /**
     * User requested to retry loading data
     */
    object Retry : PayloadDetailEvent()
    
    /**
     * User dismissed an error
     */
    object DismissError : PayloadDetailEvent()
} 