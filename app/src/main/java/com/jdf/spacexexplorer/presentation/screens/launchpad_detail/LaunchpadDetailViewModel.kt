package com.jdf.spacexexplorer.presentation.screens.launchpad_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetLaunchpadByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Launchpad detail screen.
 * Manages the UI state and business logic for displaying launchpad details.
 */
@HiltViewModel
class LaunchpadDetailViewModel @Inject constructor(
    private val getLaunchpadByIdUseCase: GetLaunchpadByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(LaunchpadDetailState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<LaunchpadDetailState> = _state.asStateFlow()
    
    init {
        // Get the launchpad ID from the navigation arguments
        val launchpadId = savedStateHandle.get<String>("launchpadId") ?: ""
        if (launchpadId.isNotEmpty()) {
            loadLaunchpad(launchpadId)
        }
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: LaunchpadDetailEvent) {
        when (event) {
            is LaunchpadDetailEvent.Retry -> {
                retry()
            }
            is LaunchpadDetailEvent.DismissError -> {
                clearError()
            }
        }
    }
    
    /**
     * Load launchpad details from the use case
     */
    private fun loadLaunchpad(launchpadId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }
            
            val result = getLaunchpadByIdUseCase(launchpadId)
            
            when (result) {
                is Result.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            launchpad = result.data,
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
                is Result.Loading -> {
                    // This shouldn't happen for a single item fetch, but handle it gracefully
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = true,
                            error = null
                        )
                    }
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
     * Retry loading launchpad details
     */
    private fun retry() {
        clearError()
        // Get the current launchpad ID from the state or saved state handle
        val currentLaunchpad = _state.value.launchpad
        if (currentLaunchpad != null) {
            loadLaunchpad(currentLaunchpad.id)
        }
    }
} 