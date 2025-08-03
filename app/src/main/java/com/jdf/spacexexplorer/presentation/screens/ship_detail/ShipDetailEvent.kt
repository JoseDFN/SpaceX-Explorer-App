package com.jdf.spacexexplorer.presentation.screens.ship_detail

/**
 * Events that can be triggered in the ship detail screen
 */
sealed class ShipDetailEvent {
    data class LoadShip(val id: String) : ShipDetailEvent()
    object DismissError : ShipDetailEvent()
} 