package com.jdf.spacexexplorer.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetLaunchesUseCase
import com.jdf.spacexexplorer.domain.usecase.GetRocketsUseCase
import com.jdf.spacexexplorer.domain.usecase.GetUpcomingLaunchesUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshLaunchesUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshRocketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen dashboard.
 * Manages the UI state and business logic for displaying the dashboard with multiple sections.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLaunchesUseCase: GetLaunchesUseCase,
    private val getUpcomingLaunchesUseCase: GetUpcomingLaunchesUseCase,
    private val getRocketsUseCase: GetRocketsUseCase,
    private val refreshLaunchesUseCase: RefreshLaunchesUseCase,
    private val refreshRocketsUseCase: RefreshRocketsUseCase
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(HomeState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    init {
        // Launch separate coroutines to fetch data for each section
        loadLatestLaunches()
        loadUpcomingLaunches()
        loadRockets()
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Refresh -> {
                refreshAllData()
            }
            is HomeEvent.Retry -> {
                retry()
            }
            is HomeEvent.DismissError -> {
                clearAllErrors()
            }
            is HomeEvent.LaunchClicked -> {
                // TODO: Navigate to launch details screen
                // This will be implemented when we add navigation
            }
            is HomeEvent.RocketClicked -> {
                // TODO: Navigate to rocket details screen
                // This will be implemented when we add navigation
            }
        }
    }
    
    /**
     * Load latest launches from the use case
     */
    private fun loadLatestLaunches() {
        viewModelScope.launch {
            getLaunchesUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLatestLaunchesLoading = true,
                                latestLaunchesError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the latest 5 launches for the dashboard
                        val latestLaunches = result.data.take(5)
                        _state.update { currentState ->
                            currentState.copy(
                                isLatestLaunchesLoading = false,
                                latestLaunches = latestLaunches,
                                latestLaunchesError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLatestLaunchesLoading = false,
                                latestLaunchesError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }

                }
            }
        }
    }
    
    /**
     * Load upcoming launches from the use case
     */
    private fun loadUpcomingLaunches() {
        viewModelScope.launch {
            getUpcomingLaunchesUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isUpcomingLaunchesLoading = true,
                                upcomingLaunchesError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the next 5 upcoming launches for the dashboard
                        val upcomingLaunches = result.data.take(5)
                        _state.update { currentState ->
                            currentState.copy(
                                isUpcomingLaunchesLoading = false,
                                upcomingLaunches = upcomingLaunches,
                                upcomingLaunchesError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isUpcomingLaunchesLoading = false,
                                upcomingLaunchesError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }

                }
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
                                isRocketsLoading = true,
                                rocketsError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the first 10 rockets for the dashboard
                        val rockets = result.data.take(10)
                        _state.update { currentState ->
                            currentState.copy(
                                isRocketsLoading = false,
                                rockets = rockets,
                                rocketsError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isRocketsLoading = false,
                                rocketsError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }

                }
            }
        }
    }
    
    /**
     * Refresh all data from the remote API
     */
    private fun refreshAllData() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            // Refresh both launches and rockets
            val launchesRefreshResult = refreshLaunchesUseCase()
            val rocketsRefreshResult = refreshRocketsUseCase()
            
            when {
                launchesRefreshResult is Result.Success && rocketsRefreshResult is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                launchesRefreshResult is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            latestLaunchesError = launchesRefreshResult.exception.message ?: "Failed to refresh launches"
                        )
                    }
                }
                rocketsRefreshResult is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            rocketsError = rocketsRefreshResult.exception.message ?: "Failed to refresh rockets"
                        )
                    }
                }

            }
        }
    }
    
    /**
     * Clear all error states
     */
    private fun clearAllErrors() {
        _state.update { 
            it.copy(
                latestLaunchesError = null,
                upcomingLaunchesError = null,
                rocketsError = null
            )
        }
    }
    
    /**
     * Retry loading all data
     */
    private fun retry() {
        clearAllErrors()
        loadLatestLaunches()
        loadUpcomingLaunches()
        loadRockets()
    }
} 