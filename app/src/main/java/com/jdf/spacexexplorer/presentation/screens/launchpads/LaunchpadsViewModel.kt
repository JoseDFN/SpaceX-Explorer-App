package com.jdf.spacexexplorer.presentation.screens.launchpads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetLaunchpadsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshLaunchpadsUseCase
import com.jdf.spacexexplorer.presentation.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Launchpads screen.
 * Manages the UI state and business logic for displaying launchpads.
 */
@HiltViewModel
class LaunchpadsViewModel @Inject constructor(
    private val getLaunchpadsUseCase: GetLaunchpadsUseCase,
    private val refreshLaunchpadsUseCase: RefreshLaunchpadsUseCase
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(LaunchpadsState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<LaunchpadsState> = _state.asStateFlow()
    
    /**
     * Navigation events channel for one-time navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()
    
    init {
        // Launch a coroutine to collect the flow from the use case
        loadLaunchpads()
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: LaunchpadsEvent) {
        when (event) {
            is LaunchpadsEvent.Refresh -> {
                refreshLaunchpads()
            }
            is LaunchpadsEvent.Retry -> {
                retry()
            }
            is LaunchpadsEvent.DismissError -> {
                clearError()
            }
            is LaunchpadsEvent.LaunchpadClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToLaunchpadDetail(event.launchpad.id))
                }
            }
        }
    }
    
    /**
     * Load launchpads from the use case
     */
    private fun loadLaunchpads() {
        viewModelScope.launch {
            getLaunchpadsUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }
                    is Result.Success -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                launchpads = result.data,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Refresh launchpads from the remote API
     */
    private fun refreshLaunchpads() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            val refreshResult = refreshLaunchpadsUseCase()
            
            when (refreshResult) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = refreshResult.exception.message ?: "Failed to refresh launchpads"
                        )
                    }
                }
                is Result.Loading -> {
                    // This shouldn't happen for refresh, but handle it gracefully
                    _state.update { it.copy(isRefreshing = false) }
                }
            }
        }
    }
    
    /**
     * Clear any error state
     */
    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
    
    /**
     * Retry loading launchpads
     */
    private fun retry() {
        clearError()
        loadLaunchpads()
    }
} 