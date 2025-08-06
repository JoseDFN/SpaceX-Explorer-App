package com.jdf.spacexexplorer.presentation.screens.payloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.usecase.GetPayloadsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshPayloadsUseCase
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
 * ViewModel for the Payloads screen.
 * Manages the UI state and business logic for displaying payloads.
 */
@HiltViewModel
class PayloadsViewModel @Inject constructor(
    private val getPayloadsUseCase: GetPayloadsUseCase,
    private val refreshPayloadsUseCase: RefreshPayloadsUseCase
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(PayloadsState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<PayloadsState> = _state.asStateFlow()
    
    /**
     * Navigation events channel for one-time navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()
    
    init {
        // Initialize available filters for payloads
        initializeAvailableFilters()
        // Launch a coroutine to collect the flow from the use case
        loadPayloads()
    }
    
    /**
     * Initialize the list of available filters for the payloads screen
     */
    private fun initializeAvailableFilters() {
        val availableFilters = listOf<FilterOption>(
            FilterOption.PayloadTypeFilter("Satellite"),
            FilterOption.PayloadTypeFilter("Dragon 1.0"),
            FilterOption.PayloadTypeFilter("Dragon 1.1"),
            FilterOption.PayloadTypeFilter("Dragon 2.0"),
            FilterOption.PayloadTypeFilter("Crew Dragon"),
            FilterOption.PayloadTypeFilter("Cargo Dragon"),
            FilterOption.PayloadNationalityFilter("United States"),
            FilterOption.PayloadNationalityFilter("Canada"),
            FilterOption.PayloadNationalityFilter("Japan"),
            FilterOption.PayloadNationalityFilter("European Space Agency"),
            FilterOption.PayloadNationalityFilter("Russia")
        )
        
        _state.update { it.copy(availableFilters = availableFilters) }
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: PayloadsEvent) {
        when (event) {
            is PayloadsEvent.Refresh -> {
                refreshPayloads()
            }
            is PayloadsEvent.Retry -> {
                retry()
            }
            is PayloadsEvent.DismissError -> {
                clearError()
            }
            is PayloadsEvent.PayloadClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToPayloadDetail(event.payload.id))
                }
            }
            is PayloadsEvent.UpdateFilter -> {
                updateFilter(event.filter)
            }
            is PayloadsEvent.RemoveFilter -> {
                removeFilter(event.filterKey)
            }
            is PayloadsEvent.ClearAllFilters -> {
                clearAllFilters()
            }
            is PayloadsEvent.UpdateSort -> {
                updateSort(event.sort)
            }
        }
    }
    
    /**
     * Update or add a filter to the active filters
     */
    private fun updateFilter(filter: FilterOption) {
        val filterKey = when (filter) {
            is FilterOption.PayloadTypeFilter -> "type_${filter.type}"
            is FilterOption.PayloadNationalityFilter -> "nationality_${filter.nationality}"
            else -> filter::class.simpleName ?: "unknown"
        }
        
        _state.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters + (filterKey to filter)
            )
        }
        
        // Reload payloads with new filters
        loadPayloads()
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
        
        // Reload payloads with updated filters
        loadPayloads()
    }
    
    /**
     * Clear all active filters
     */
    private fun clearAllFilters() {
        _state.update { it.copy(activeFilters = emptyMap()) }
        
        // Reload payloads without filters
        loadPayloads()
    }
    
    /**
     * Update the current sort option
     */
    private fun updateSort(sort: SortOption) {
        _state.update { it.copy(currentSort = sort) }
        
        // Reload payloads with new sort
        loadPayloads()
    }
    
    /**
     * Load payloads from the use case
     */
    private fun loadPayloads() {
        val currentState = _state.value
        
        viewModelScope.launch {
            getPayloadsUseCase(
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
                                payloads = result.data,
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
     * Refresh payloads from the remote API
     */
    private fun refreshPayloads() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            val refreshResult = refreshPayloadsUseCase()
            
            when (refreshResult) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = refreshResult.exception.message ?: "Failed to refresh payloads"
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
     * Retry loading payloads
     */
    private fun retry() {
        clearError()
        loadPayloads()
    }
} 