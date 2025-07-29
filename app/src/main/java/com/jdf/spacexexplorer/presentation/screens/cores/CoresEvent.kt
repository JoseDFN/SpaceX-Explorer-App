package com.jdf.spacexexplorer.presentation.screens.cores

import com.jdf.spacexexplorer.domain.model.Core

/**
 * UI events for the Cores screen
 */
sealed class CoresEvent {
    object Refresh : CoresEvent()
    data class CoreClicked(val core: Core) : CoresEvent()
    object Retry : CoresEvent()
    object DismissError : CoresEvent()
} 