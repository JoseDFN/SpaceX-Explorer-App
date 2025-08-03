package com.jdf.spacexexplorer.presentation.screens.dragons

import com.jdf.spacexexplorer.domain.model.Dragon

/**
 * Sealed class representing all possible UI events for the Dragons screen
 */
sealed class DragonsEvent {
    /**
     * User requested to refresh data
     */
    object Refresh : DragonsEvent()
    
    /**
     * User clicked on a dragon item
     */
    data class DragonClicked(val dragon: Dragon) : DragonsEvent()
    
    /**
     * User requested to retry loading data
     */
    object Retry : DragonsEvent()
    
    /**
     * User dismissed an error
     */
    object DismissError : DragonsEvent()
} 