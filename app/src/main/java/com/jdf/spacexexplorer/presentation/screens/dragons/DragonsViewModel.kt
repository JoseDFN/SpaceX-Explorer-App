package com.jdf.spacexexplorer.presentation.screens.dragons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.usecase.GetDragonsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshDragonsUseCase
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
        // Initialize available filters for dragons
        initializeAvailableFilters()
        // Set initial loading state
        _state.update { it.copy(isLoading = true) }
        // Launch a coroutine to collect the flow from the use case
        loadDragons()
        // Trigger initial refresh in background
        viewModelScope.launch {
            refreshDragonsUseCase()
        }
    }
    
    /**
     * Initialize the list of available filters for the dragons screen
     */
    private fun initializeAvailableFilters() {
        val availableFilters = listOf<FilterOption>(
            FilterOption.DragonActiveFilter(true),
            FilterOption.DragonActiveFilter(false),
            FilterOption.DragonTypeFilter("Dragon 1.0"),
            FilterOption.DragonTypeFilter("Dragon 1.1"),
            FilterOption.DragonTypeFilter("Dragon 2.0")
        )
        
        _state.update { it.copy(availableFilters = availableFilters) }
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
                    _navigationEvents.send(NavigationEvent.NavigateToDragonDetail(event.dragonId))
                }
            }
            is DragonsEvent.UpdateFilter -> {
                updateFilter(event.filter)
            }
            is DragonsEvent.RemoveFilter -> {
                removeFilter(event.filterKey)
            }
            is DragonsEvent.ClearAllFilters -> {
                clearAllFilters()
            }
            is DragonsEvent.UpdateSort -> {
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
            is FilterOption.DragonActiveFilter -> "active_${filter.active}"
            is FilterOption.DragonTypeFilter -> "type_${filter.type}"
            else -> filter::class.simpleName ?: "unknown"
        }
        
        _state.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters + (filterKey to filter)
            )
        }
        
        // Reload dragons with new filters
        loadDragons()
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
        
        // Reload dragons with updated filters
        loadDragons()
    }
    
    /**
     * Clear all active filters
     */
    private fun clearAllFilters() {
        _state.update { it.copy(activeFilters = emptyMap()) }
        
        // Reload dragons without filters
        loadDragons()
    }
    
    /**
     * Update the current sort option
     */
    private fun updateSort(sort: SortOption) {
        _state.update { it.copy(currentSort = sort) }
        
        // Reload dragons with new sort
        loadDragons()
    }
    
    /**
     * Load dragons from the use case
     */
    private fun loadDragons() {
        val currentState = _state.value
        
        viewModelScope.launch {
            getDragonsUseCase(
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