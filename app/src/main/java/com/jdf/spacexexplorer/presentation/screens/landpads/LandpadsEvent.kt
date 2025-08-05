package com.jdf.spacexexplorer.presentation.screens.landpads

import com.jdf.spacexexplorer.domain.model.Landpad

/**
 * Sealed class representing all possible UI events for the Landpads screen
 */
sealed class LandpadsEvent {
    /**
     * User requested to refresh data
     */
    object Refresh : LandpadsEvent()
    
    /**
     * User clicked on a landpad item
     */
    data class LandpadClicked(val landpad: Landpad) : LandpadsEvent()
    
    /**
     * User requested to retry loading data
     */
    object Retry : LandpadsEvent()
    
    /**
     * User dismissed an error
     */
    object DismissError : LandpadsEvent()
} 