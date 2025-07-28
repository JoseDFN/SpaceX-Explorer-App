package com.jdf.spacexexplorer.presentation.screens.capsules

import com.jdf.spacexexplorer.domain.model.Capsule

/**
 * UI events for the Capsules screen
 */
sealed class CapsulesEvent {
    object Refresh : CapsulesEvent()
    data class CapsuleClicked(val capsule: Capsule) : CapsulesEvent()
    object Retry : CapsulesEvent()
    object DismissError : CapsulesEvent()
} 