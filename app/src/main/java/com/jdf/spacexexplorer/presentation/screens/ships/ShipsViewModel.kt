package com.jdf.spacexexplorer.presentation.screens.ships

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.usecase.GetShipsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshShipsUseCase
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
 * ViewModel for the Ships screen.
 * Manages the UI state and business logic for displaying ships.
 */
@HiltViewModel
class ShipsViewModel @Inject constructor(
    private val getShipsUseCase: GetShipsUseCase,
    private val refreshShipsUseCase: RefreshShipsUseCase
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(ShipsState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<ShipsState> = _state.asStateFlow()
    
    /**
     * Navigation events channel for one-time navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()
    
    init {
        // Initialize available filters for ships
        initializeAvailableFilters()
        // Launch a coroutine to collect the flow from the use case
        loadShips()
    }
    
    /**
     * Initialize the list of available filters for the ships screen
     */
    private fun initializeAvailableFilters() {
        val availableFilters = listOf<FilterOption>(
            FilterOption.ShipActiveFilter(true),
            FilterOption.ShipActiveFilter(false),
            FilterOption.ShipTypeFilter("Cargo"),
            FilterOption.ShipTypeFilter("Tug"),
            FilterOption.ShipTypeFilter("Barge"),
            FilterOption.ShipTypeFilter("High Speed Craft")
        )
        
        _state.update { it.copy(availableFilters = availableFilters) }
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: ShipsEvent) {
        when (event) {
            is ShipsEvent.Refresh -> {
                refreshShips()
            }
            is ShipsEvent.Retry -> {
                retry()
            }
            is ShipsEvent.DismissError -> {
                clearError()
            }
            is ShipsEvent.ShipClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToShipDetail(event.ship.id))
                }
            }
            is ShipsEvent.UpdateFilter -> {
                updateFilter(event.filter)
            }
            is ShipsEvent.RemoveFilter -> {
                removeFilter(event.filterKey)
            }
            is ShipsEvent.ClearAllFilters -> {
                clearAllFilters()
            }
            is ShipsEvent.UpdateSort -> {
                updateSort(event.sort)
            }
        }
    }
    
    /**
     * Update or add a filter to the active filters
     */
    private fun updateFilter(filter: FilterOption) {
        val filterKey = when (filter) {
            is FilterOption.ShipActiveFilter -> "active_${filter.active}"
            is FilterOption.ShipTypeFilter -> "type_${filter.type}"
            else -> filter::class.simpleName ?: "unknown"
        }
        
        _state.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters + (filterKey to filter)
            )
        }
        
        // Reload ships with new filters
        loadShips()
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
        
        // Reload ships with updated filters
        loadShips()
    }
    
    /**
     * Clear all active filters
     */
    private fun clearAllFilters() {
        _state.update { it.copy(activeFilters = emptyMap()) }
        
        // Reload ships without filters
        loadShips()
    }
    
    /**
     * Update the current sort option
     */
    private fun updateSort(sort: SortOption) {
        _state.update { it.copy(currentSort = sort) }
        
        // Reload ships with new sort
        loadShips()
    }
    
    /**
     * Load ships from the use case
     */
    private fun loadShips() {
        val currentState = _state.value
        
        viewModelScope.launch {
            getShipsUseCase(
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
                                ships = result.data,
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
     * Refresh ships from the remote API
     */
    private fun refreshShips() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            val refreshResult = refreshShipsUseCase()
            
            when (refreshResult) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = refreshResult.exception.message ?: "Failed to refresh ships"
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
     * Retry loading ships
     */
    private fun retry() {
        clearError()
        loadShips()
    }
} 