package com.jdf.spacexexplorer.presentation.screens.landpads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetLandpadsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshLandpadsUseCase
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
 * ViewModel for the Landpads screen.
 * Manages the UI state and business logic for displaying landpads.
 */
@HiltViewModel
class LandpadsViewModel @Inject constructor(
    private val getLandpadsUseCase: GetLandpadsUseCase,
    private val refreshLandpadsUseCase: RefreshLandpadsUseCase
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(LandpadsState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<LandpadsState> = _state.asStateFlow()
    
    /**
     * Navigation events channel for one-time navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()
    
    init {
        // Launch a coroutine to collect the flow from the use case
        loadLandpads()
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: LandpadsEvent) {
        when (event) {
            is LandpadsEvent.Refresh -> {
                refreshLandpads()
            }
            is LandpadsEvent.Retry -> {
                retry()
            }
            is LandpadsEvent.DismissError -> {
                clearError()
            }
            is LandpadsEvent.LandpadClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToLandpadDetail(event.landpad.id))
                }
            }
        }
    }
    
    /**
     * Load landpads from the use case
     */
    private fun loadLandpads() {
        viewModelScope.launch {
            getLandpadsUseCase().collect { result ->
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
                                landpads = result.data,
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
     * Refresh landpads from the remote API
     */
    private fun refreshLandpads() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            val refreshResult = refreshLandpadsUseCase()
            
            when (refreshResult) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = refreshResult.exception.message ?: "Failed to refresh landpads"
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
     * Retry loading landpads
     */
    private fun retry() {
        clearError()
        loadLandpads()
    }
} 