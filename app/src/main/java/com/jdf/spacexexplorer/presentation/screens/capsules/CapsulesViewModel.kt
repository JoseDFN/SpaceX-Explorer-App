package com.jdf.spacexexplorer.presentation.screens.capsules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.usecase.GetCapsulesUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshCapsulesUseCase
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
 * ViewModel for the Capsules screen
 */
@HiltViewModel
class CapsulesViewModel @Inject constructor(
    private val getCapsulesUseCase: GetCapsulesUseCase,
    private val refreshCapsulesUseCase: RefreshCapsulesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CapsulesState())
    val state: StateFlow<CapsulesState> = _state.asStateFlow()
    
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        // Initialize available filters for capsules
        initializeAvailableFilters()
        loadCapsules()
    }
    
    /**
     * Initialize the list of available filters for the capsules screen
     */
    private fun initializeAvailableFilters() {
        val availableFilters = listOf<FilterOption>(
            FilterOption.CapsuleTypeFilter("Dragon 1.0"),
            FilterOption.CapsuleTypeFilter("Dragon 1.1"),
            FilterOption.CapsuleTypeFilter("Dragon 2.0"),
            FilterOption.CapsuleStatusFilter("active"),
            FilterOption.CapsuleStatusFilter("retired"),
            FilterOption.CapsuleStatusFilter("unknown")
        )
        
        _state.update { it.copy(availableFilters = availableFilters) }
    }

    /**
     * Handle UI events from the user
     */
    fun onEvent(event: CapsulesEvent) {
        when (event) {
            is CapsulesEvent.Refresh -> {
                refreshCapsules()
            }
            is CapsulesEvent.Retry -> {
                retry()
            }
            is CapsulesEvent.DismissError -> {
                clearError()
            }
            is CapsulesEvent.CapsuleClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToCapsuleDetail(event.capsule.id))
                }
            }
            is CapsulesEvent.UpdateFilter -> {
                updateFilter(event.filter)
            }
            is CapsulesEvent.RemoveFilter -> {
                removeFilter(event.filterKey)
            }
            is CapsulesEvent.ClearAllFilters -> {
                clearAllFilters()
            }
            is CapsulesEvent.UpdateSort -> {
                updateSort(event.sort)
            }
        }
    }
    
    /**
     * Update or add a filter to the active filters
     */
    private fun updateFilter(filter: FilterOption) {
        val filterKey = when (filter) {
            is FilterOption.CapsuleTypeFilter -> "type_${filter.type}"
            is FilterOption.CapsuleStatusFilter -> "status_${filter.status}"
            else -> filter::class.simpleName ?: "unknown"
        }
        
        _state.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters + (filterKey to filter)
            )
        }
        
        // Reload capsules with new filters
        loadCapsules()
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
        
        // Reload capsules with updated filters
        loadCapsules()
    }
    
    /**
     * Clear all active filters
     */
    private fun clearAllFilters() {
        _state.update { it.copy(activeFilters = emptyMap()) }
        
        // Reload capsules without filters
        loadCapsules()
    }
    
    /**
     * Update the current sort option
     */
    private fun updateSort(sort: SortOption) {
        _state.update { it.copy(currentSort = sort) }
        
        // Reload capsules with new sort
        loadCapsules()
    }

    /**
     * Load capsules from the use case
     */
    private fun loadCapsules() {
        val currentState = _state.value
        
        viewModelScope.launch {
            getCapsulesUseCase(
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
                                capsules = result.data,
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
     * Refresh capsules from the remote API
     */
    private fun refreshCapsules() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            val refreshResult = refreshCapsulesUseCase()
            
            when (refreshResult) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = refreshResult.exception.message ?: "Failed to refresh capsules"
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
     * Retry loading capsules
     */
    private fun retry() {
        clearError()
        loadCapsules()
    }
} 