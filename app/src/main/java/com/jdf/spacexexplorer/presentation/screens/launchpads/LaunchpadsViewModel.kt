package com.jdf.spacexexplorer.presentation.screens.launchpads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.usecase.GetLaunchpadsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshLaunchpadsUseCase
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
        // Initialize available filters for launchpads
        initializeAvailableFilters()
        // Launch a coroutine to collect the flow from the use case
        loadLaunchpads()
        // Trigger initial refresh in background
        viewModelScope.launch {
            refreshLaunchpadsUseCase()
        }
    }
    
    /**
     * Initialize the list of available filters for the launchpads screen
     */
    private fun initializeAvailableFilters() {
        val availableFilters = listOf<FilterOption>(
            FilterOption.LaunchpadStatusFilter("active"),
            FilterOption.LaunchpadStatusFilter("inactive"),
            FilterOption.LaunchpadStatusFilter("unknown"),
            FilterOption.LaunchpadStatusFilter("retired"),
            FilterOption.LaunchpadStatusFilter("lost"),
            FilterOption.LaunchpadRegionFilter("Florida"),
            FilterOption.LaunchpadRegionFilter("California"),
            FilterOption.LaunchpadRegionFilter("Texas"),
            FilterOption.LaunchpadRegionFilter("Vandenberg")
        )
        
        _state.update { it.copy(availableFilters = availableFilters) }
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
            is LaunchpadsEvent.UpdateFilter -> {
                updateFilter(event.filter)
            }
            is LaunchpadsEvent.RemoveFilter -> {
                removeFilter(event.filterKey)
            }
            is LaunchpadsEvent.ClearAllFilters -> {
                clearAllFilters()
            }
            is LaunchpadsEvent.UpdateSort -> {
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
            is FilterOption.LaunchpadStatusFilter -> "status_${filter.status}"
            is FilterOption.LaunchpadRegionFilter -> "region_${filter.region}"
            else -> filter::class.simpleName ?: "unknown"
        }
        
        _state.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters + (filterKey to filter)
            )
        }
        
        // Reload launchpads with new filters
        loadLaunchpads()
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
        
        // Reload launchpads with updated filters
        loadLaunchpads()
    }
    
    /**
     * Clear all active filters
     */
    private fun clearAllFilters() {
        _state.update { it.copy(activeFilters = emptyMap()) }
        
        // Reload launchpads without filters
        loadLaunchpads()
    }
    
    /**
     * Update the current sort option
     */
    private fun updateSort(sort: SortOption) {
        _state.update { it.copy(currentSort = sort) }
        
        // Reload launchpads with new sort
        loadLaunchpads()
    }
    
    /**
     * Load launchpads from the use case
     */
    private fun loadLaunchpads() {
        val currentState = _state.value
        
        viewModelScope.launch {
            getLaunchpadsUseCase(
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