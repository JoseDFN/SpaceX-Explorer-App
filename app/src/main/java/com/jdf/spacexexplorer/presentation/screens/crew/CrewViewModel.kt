package com.jdf.spacexexplorer.presentation.screens.crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.usecase.GetCrewUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshCrewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the crew screen
 */
@HiltViewModel
class CrewViewModel @Inject constructor(
    private val getCrewUseCase: GetCrewUseCase,
    private val refreshCrewUseCase: RefreshCrewUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CrewState())
    val state: StateFlow<CrewState> = _state.asStateFlow()

    init {
        // Initialize available filters for crew
        initializeAvailableFilters()
        loadCrew()
    }
    
    /**
     * Initialize the list of available filters for the crew screen
     */
    private fun initializeAvailableFilters() {
        val availableFilters = listOf<FilterOption>(
            FilterOption.CrewAgencyFilter("NASA"),
            FilterOption.CrewAgencyFilter("ESA"),
            FilterOption.CrewAgencyFilter("JAXA"),
            FilterOption.CrewAgencyFilter("CSA"),
            FilterOption.CrewAgencyFilter("Roscosmos"),
            FilterOption.CrewStatusFilter("active"),
            FilterOption.CrewStatusFilter("inactive"),
            FilterOption.CrewStatusFilter("retired"),
            FilterOption.CrewStatusFilter("unknown")
        )
        
        _state.update { it.copy(availableFilters = availableFilters) }
    }

    fun onEvent(event: CrewEvent) {
        when (event) {
            is CrewEvent.LoadCrew -> loadCrew()
            is CrewEvent.RefreshCrew -> refreshCrew()
            is CrewEvent.DismissError -> dismissError()
            is CrewEvent.UpdateFilter -> {
                updateFilter(event.filter)
            }
            is CrewEvent.RemoveFilter -> {
                removeFilter(event.filterKey)
            }
            is CrewEvent.ClearAllFilters -> {
                clearAllFilters()
            }
            is CrewEvent.UpdateSort -> {
                updateSort(event.sort)
            }
        }
    }
    
    /**
     * Update or add a filter to the active filters
     */
    private fun updateFilter(filter: FilterOption) {
        val filterKey = when (filter) {
            is FilterOption.CrewAgencyFilter -> "agency_${filter.agency}"
            is FilterOption.CrewStatusFilter -> "status_${filter.status}"
            else -> filter::class.simpleName ?: "unknown"
        }
        
        _state.update { currentState ->
            currentState.copy(
                activeFilters = currentState.activeFilters + (filterKey to filter)
            )
        }
        
        // Reload crew with new filters
        loadCrew()
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
        
        // Reload crew with updated filters
        loadCrew()
    }
    
    /**
     * Clear all active filters
     */
    private fun clearAllFilters() {
        _state.update { it.copy(activeFilters = emptyMap()) }
        
        // Reload crew without filters
        loadCrew()
    }
    
    /**
     * Update the current sort option
     */
    private fun updateSort(sort: SortOption) {
        _state.update { it.copy(currentSort = sort) }
        
        // Reload crew with new sort
        loadCrew()
    }

    private fun loadCrew() {
        val currentState = _state.value
        
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getCrewUseCase(
                filters = currentState.activeFiltersList,
                sort = currentState.currentSort
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _state.update { 
                            it.copy(
                                crew = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { 
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun refreshCrew() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }
            
            when (val result = refreshCrewUseCase()) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                }
                is Result.Loading -> {
                    _state.update { it.copy(isRefreshing = true) }
                }
            }
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
} 