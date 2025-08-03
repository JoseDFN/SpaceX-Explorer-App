package com.jdf.spacexexplorer.presentation.screens.launches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetLaunchesUseCase
import com.jdf.spacexexplorer.domain.usecase.GetLaunchesPageUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshLaunchesUseCase
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
 * ViewModel for the Launches screen.
 * Manages the UI state and business logic for displaying launches.
 */
@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val getLaunchesUseCase: GetLaunchesUseCase,
    private val getLaunchesPageUseCase: GetLaunchesPageUseCase,
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
    
    /**
     * Navigation events channel for one-time navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()
    
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
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToLaunchDetail(event.launchId))
                }
            }
            is LaunchesEvent.LoadMore -> {
                loadMoreLaunches()
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
     * Load more launches for pagination
     */
    private fun loadMoreLaunches() {
        val currentState = _state.value
        
        // Don't load more if already loading or if end is reached
        if (currentState.isLoadingMore || currentState.endReached) {
            return
        }
        
        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }
            
            val nextPage = currentState.currentPage + 1
            val result = getLaunchesPageUseCase(nextPage)
            
            when (result) {
                is Result.Success -> {
                    val newLaunches = result.data
                    if (newLaunches.isEmpty()) {
                        // No more data available
                        _state.update { 
                            it.copy(
                                isLoadingMore = false,
                                endReached = true
                            )
                        }
                    } else {
                        // Append new launches to existing list
                        _state.update { 
                            it.copy(
                                launches = it.launches + newLaunches,
                                isLoadingMore = false,
                                currentPage = nextPage
                            )
                        }
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isLoadingMore = false,
                            error = result.exception.message ?: "Failed to load more launches"
                        )
                    }
                }
                is Result.Loading -> {
                    // This shouldn't happen for pagination, but handle it gracefully
                    _state.update { it.copy(isLoadingMore = false) }
                }
            }
        }
    }
    
    /**
     * Retry loading launches
     */
    private fun retry() {
        clearError()
        loadLaunches()
    }
} 