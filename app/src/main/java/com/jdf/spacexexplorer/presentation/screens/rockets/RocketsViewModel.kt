package com.jdf.spacexexplorer.presentation.screens.rockets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.usecase.GetRocketsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshRocketsUseCase
import com.jdf.spacexexplorer.presentation.components.FilterEvent
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
    
    /**
     * Navigation events channel for one-time navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()
    
    init {
        // Initialize available filters for rockets
        initializeAvailableFilters()
        // Launch a coroutine to collect the flow from the use case
        loadRockets()
        // Trigger initial refresh in background
        viewModelScope.launch {
            refreshRocketsUseCase()
        }
    }
    
    /**
     * Initialize the list of available filters for the rockets screen
     */
    private fun initializeAvailableFilters() {
        val availableFilters = listOf<FilterOption>(
            FilterOption.RocketActiveFilter(true),
            FilterOption.RocketActiveFilter(false),
            FilterOption.RocketTypeFilter("Falcon 9"),
            FilterOption.RocketTypeFilter("Falcon Heavy"),
            FilterOption.RocketTypeFilter("Starship")
            // Add more filter options as needed
        )
        
        _state.update { it.copy(availableFilters = availableFilters) }
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
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToRocketDetail(event.rocket.id))
                }
            }
            is RocketsEvent.UpdateFilter -> {
                updateFilter(event.filter)
            }
            is RocketsEvent.RemoveFilter -> {
                removeFilter(event.filterKey)
            }
            is RocketsEvent.ClearAllFilters -> {
                clearAllFilters()
            }
            is RocketsEvent.UpdateSort -> {
                updateSort(event.sort)
            }
        }
    }
    
    /**
     * Handle generic filter events from the FilterBar component
     */
    fun onFilterEvent(event: FilterEvent) {
        when (event) {
            is FilterEvent.UpdateFilter -> {
                updateFilter(event.filter)
            }
            is FilterEvent.RemoveFilter -> {
                removeFilter(event.filterKey)
            }
            is FilterEvent.ClearAllFilters -> {
                clearAllFilters()
            }
        }
    }
    
    /**
     * Update or add a filter to the active filters
     */
    private fun updateFilter(filter: FilterOption) {
        val filterKey = when (filter) {
            is FilterOption.RocketActiveFilter -> "active_${filter.active}"
            is FilterOption.RocketTypeFilter -> "type_${filter.type}"
            else -> filter::class.simpleName ?: "unknown"
        }
        
        _state.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters + (filterKey to filter)
            )
        }
        
        // Reload rockets with new filters
        loadRockets()
    }
    
    /**
     * Remove a specific filter from active filters
     */
    private fun removeFilter(filterKey: String) {
        _state.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters - filterKey
            )
        }
        
        // Reload rockets with updated filters
        loadRockets()
    }
    
    /**
     * Clear all active filters
     */
    private fun clearAllFilters() {
        _state.update { it.copy(activeFilters = emptyMap()) }
        
        // Reload rockets without filters
        loadRockets()
    }
    
    /**
     * Update the current sort option
     */
    private fun updateSort(sort: SortOption) {
        _state.update { it.copy(currentSort = sort) }
        
        // Reload rockets with new sort
        loadRockets()
    }
    
    /**
     * Load rockets from the use case
     */
    private fun loadRockets() {
        val currentState = _state.value
        
        viewModelScope.launch {
            getRocketsUseCase(
                filters = currentState.activeFiltersList,
                sort = currentState.currentSort
            ).collect { result ->
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