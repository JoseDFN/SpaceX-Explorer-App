package com.jdf.spacexexplorer.presentation.screens.crew

/**
 * Events that can be triggered in the crew screen
 */
sealed class CrewEvent {
    object LoadCrew : CrewEvent()
    object RefreshCrew : CrewEvent()
    object DismissError : CrewEvent()
} 