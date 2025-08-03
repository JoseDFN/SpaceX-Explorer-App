package com.jdf.spacexexplorer.presentation.screens.dragons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetDragonsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshDragonsUseCase
import com.jdf.spacexexplorer.presentation.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Dragons screen.
 * Manages the UI state and business logic for displaying dragons.
 */
@HiltViewModel
class DragonsViewModel @Inject constructor(
    private val getDragonsUseCase: GetDragonsUseCase,
    private val refreshDragonsUseCase: RefreshDragonsUseCase
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(DragonsState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<DragonsState> = _state.asStateFlow()
    
    /**
     * Navigation events channel for one-time navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()
    
    init {
        // Set initial loading state
        _state.update { it.copy(isLoading = true) }
        // Launch a coroutine to collect the flow from the use case
        loadDragons()
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: DragonsEvent) {
        when (event) {
            is DragonsEvent.Refresh -> {
                refreshDragons()
            }
            is DragonsEvent.Retry -> {
                retry()
            }
            is DragonsEvent.DismissError -> {
                clearError()
            }
            is DragonsEvent.DragonClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToDragonDetail(event.dragon.id))
                }
            }
        }
    }
    
    /**
     * Load dragons from the use case
     */
    private fun loadDragons() {
        viewModelScope.launch {
            getDragonsUseCase().collect { result ->
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
                                dragons = result.data,
                                error = null
                            )
                        }
                        // Log the number of dragons loaded for debugging
                        println("Dragons loaded: ${result.data.size}")
                        
                        // If no dragons loaded and we're not in error state, show a message
                        if (result.data.isEmpty()) {
                            _state.update { currentState ->
                                currentState.copy(
                                    error = "No dragons found. Please check your internet connection and try again."
                                )
                            }
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                        // Log the error for debugging
                        println("Error loading dragons: ${result.exception.message}")
                        result.exception.printStackTrace()
                    }

                }
            }
        }
    }
    
    /**
     * Refresh dragons from the remote API
     */
    private fun refreshDragons() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            val refreshResult = refreshDragonsUseCase()
            
            when (refreshResult) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = refreshResult.exception.message ?: "Failed to refresh dragons"
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
     * Retry loading dragons
     */
    private fun retry() {
        clearError()
        loadDragons()
    }
} 