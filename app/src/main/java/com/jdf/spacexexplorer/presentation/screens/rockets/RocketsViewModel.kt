package com.jdf.spacexexplorer.presentation.screens.rockets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetRocketsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshRocketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Rockets screen.
 * Manages the UI state and business logic for displaying rockets.
 */
@HiltViewModel
class RocketsViewModel @Inject constructor(
    private val getRocketsUseCase: GetRocketsUseCase,
    private val refreshRocketsUseCase: RefreshRocketsUseCase
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(RocketsState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<RocketsState> = _state.asStateFlow()
    
    init {
        // Launch a coroutine to collect the flow from the use case
        loadRockets()
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: RocketsEvent) {
        when (event) {
            is RocketsEvent.Refresh -> {
                refreshRockets()
            }
            is RocketsEvent.Retry -> {
                retry()
            }
            is RocketsEvent.DismissError -> {
                clearError()
            }
            is RocketsEvent.RocketClicked -> {
                // TODO: Navigate to rocket details screen
                // This will be implemented when we add navigation
            }
        }
    }
    
    /**
     * Load rockets from the use case
     */
    private fun loadRockets() {
        viewModelScope.launch {
            getRocketsUseCase().collect { result ->
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
                                rockets = result.data,
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
     * Refresh rockets from the remote API
     */
    private fun refreshRockets() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            val refreshResult = refreshRocketsUseCase()
            
            when (refreshResult) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = refreshResult.exception.message ?: "Failed to refresh rockets"
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
     * Retry loading rockets
     */
    private fun retry() {
        clearError()
        loadRockets()
    }
} 