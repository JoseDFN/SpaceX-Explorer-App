package com.jdf.spacexexplorer.presentation.screens.ships

/**
 * Events that can be triggered in the ships list screen
 */
sealed class ShipsEvent {
    object LoadShips : ShipsEvent()
    object RefreshShips : ShipsEvent()
    object DismissError : ShipsEvent()
} 