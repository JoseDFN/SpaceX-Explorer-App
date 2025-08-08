package com.jdf.spacexexplorer.presentation.screens.cores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.usecase.GetCoresUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshCoresUseCase
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
 * ViewModel for the Cores screen
 */
@HiltViewModel
class CoresViewModel @Inject constructor(
    private val getCoresUseCase: GetCoresUseCase,
    private val refreshCoresUseCase: RefreshCoresUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoresState())
    val state: StateFlow<CoresState> = _state.asStateFlow()
    
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        // Initialize available filters for cores
        initializeAvailableFilters()
        loadCores()
        // Trigger initial refresh in background
        viewModelScope.launch {
            refreshCoresUseCase()
        }
    }
    
    /**
     * Initialize the list of available filters for the cores screen
     */
    private fun initializeAvailableFilters() {
        val availableFilters = listOf<FilterOption>(
            FilterOption.CoreStatusFilter("active"),
            FilterOption.CoreStatusFilter("inactive"),
            FilterOption.CoreStatusFilter("unknown"),
            FilterOption.CoreStatusFilter("lost"),
            FilterOption.CoreStatusFilter("expended"),
            FilterOption.CoreBlockFilter(1),
            FilterOption.CoreBlockFilter(2),
            FilterOption.CoreBlockFilter(3),
            FilterOption.CoreBlockFilter(4),
            FilterOption.CoreBlockFilter(5)
        )
        
        _state.update { it.copy(availableFilters = availableFilters) }
    }

    /**
     * Handle UI events from the user
     */
    fun onEvent(event: CoresEvent) {
        when (event) {
            is CoresEvent.Refresh -> {
                refreshCores()
            }
            is CoresEvent.Retry -> {
                retry()
            }
            is CoresEvent.DismissError -> {
                clearError()
            }
            is CoresEvent.CoreClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToCoreDetail(event.core.id))
                }
            }
            is CoresEvent.UpdateFilter -> {
                updateFilter(event.filter)
            }
            is CoresEvent.RemoveFilter -> {
                removeFilter(event.filterKey)
            }
            is CoresEvent.ClearAllFilters -> {
                clearAllFilters()
            }
            is CoresEvent.UpdateSort -> {
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
            is FilterOption.CoreStatusFilter -> "status_${filter.status}"
            is FilterOption.CoreBlockFilter -> "block_${filter.block ?: "unknown"}"
            else -> filter::class.simpleName ?: "unknown"
        }
        
        _state.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters + (filterKey to filter)
            )
        }
        
        // Reload cores with new filters
        loadCores()
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
        
        // Reload cores with updated filters
        loadCores()
    }
    
    /**
     * Clear all active filters
     */
    private fun clearAllFilters() {
        _state.update { it.copy(activeFilters = emptyMap()) }
        
        // Reload cores without filters
        loadCores()
    }
    
    /**
     * Update the current sort option
     */
    private fun updateSort(sort: SortOption) {
        _state.update { it.copy(currentSort = sort) }
        
        // Reload cores with new sort
        loadCores()
    }

    /**
     * Load cores from the use case
     */
    private fun loadCores() {
        val currentState = _state.value
        
        viewModelScope.launch {
            getCoresUseCase(
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
                                cores = result.data,
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
     * Refresh cores from the remote API
     */
    private fun refreshCores() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            val refreshResult = refreshCoresUseCase()
            
            when (refreshResult) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = refreshResult.exception.message ?: "Failed to refresh cores"
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
     * Retry loading cores
     */
    private fun retry() {
        clearError()
        loadCores()
    }
} 