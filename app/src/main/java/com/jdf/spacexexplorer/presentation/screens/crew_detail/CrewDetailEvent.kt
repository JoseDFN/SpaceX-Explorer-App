package com.jdf.spacexexplorer.presentation.screens.crew_detail

/**
 * Events that can be triggered in the crew detail screen
 */
sealed class CrewDetailEvent {
    data class LoadCrewMember(val id: String) : CrewDetailEvent()
    object DismissError : CrewDetailEvent()
} 