package com.jdf.spacexexplorer.presentation.screens.launches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetLaunchesUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshLaunchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Launches screen.
 * Manages the UI state and business logic for displaying launches.
 */
@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val getLaunchesUseCase: GetLaunchesUseCase,
    private val refreshLaunchesUseCase: RefreshLaunchesUseCase
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(LaunchesState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<LaunchesState> = _state.asStateFlow()
    
    init {
        // Launch a coroutine to collect the flow from the use case
        loadLaunches()
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: LaunchesEvent) {
        when (event) {
            is LaunchesEvent.Refresh -> {
                refreshLaunches()
            }
            is LaunchesEvent.Retry -> {
                retry()
            }
            is LaunchesEvent.DismissError -> {
                clearError()
            }
            is LaunchesEvent.LaunchClicked -> {
                // TODO: Navigate to launch details screen
                // This will be implemented when we add navigation
            }
            is LaunchesEvent.LoadMore -> {
                // TODO: Implement pagination
                // This will be implemented when we add pagination support
            }
        }
    }
    
    /**
     * Load launches from the use case
     */
    private fun loadLaunches() {
        viewModelScope.launch {
            getLaunchesUseCase().collect { result ->
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
                                launches = result.data,
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
     * Refresh launches from the remote API
     */
    private fun refreshLaunches() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            val refreshResult = refreshLaunchesUseCase()
            
            when (refreshResult) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = refreshResult.exception.message ?: "Failed to refresh launches"
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
     * Retry loading launches
     */
    private fun retry() {
        clearError()
        loadLaunches()
    }
} 