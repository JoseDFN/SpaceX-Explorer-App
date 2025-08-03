package com.jdf.spacexexplorer.presentation.shared

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Shared state for the refresh button functionality
 */
data class SharedState(
    val onRefresh: (() -> Unit)? = null
)

/**
 * Shared ViewModel that can be accessed by both AppShell and individual screen composables
 * to handle the refresh button functionality
 */
@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    
    private val _state = MutableStateFlow(SharedState())
    val state: StateFlow<SharedState> = _state.asStateFlow()
    
    /**
     * Register a refresh handler for the current screen
     */
    fun registerRefreshHandler(handler: () -> Unit) {
        _state.update { it.copy(onRefresh = handler) }
    }
    
    /**
     * Clear the refresh handler when leaving a screen
     */
    fun clearRefreshHandler() {
        _state.update { it.copy(onRefresh = null) }
    }
} 