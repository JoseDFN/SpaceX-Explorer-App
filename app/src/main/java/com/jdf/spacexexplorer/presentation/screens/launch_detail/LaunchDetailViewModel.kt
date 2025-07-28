package com.jdf.spacexexplorer.presentation.screens.launch_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetLaunchByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Launch Detail screen.
 * Manages the UI state and business logic for displaying a single launch.
 */
@HiltViewModel
class LaunchDetailViewModel @Inject constructor(
    private val getLaunchByIdUseCase: GetLaunchByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(LaunchDetailState())

    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<LaunchDetailState> = _state.asStateFlow()

    init {
        // Retrieve launch ID from navigation arguments
        val launchId = savedStateHandle.get<String>("launchId")
        
        if (launchId != null) {
            loadLaunchById(launchId)
        } else {
            _state.update { currentState ->
                currentState.copy(
                    error = "Launch ID not provided"
                )
            }
        }
    }

    /**
     * Load a specific launch by ID
     */
    private fun loadLaunchById(launchId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }

            val result = getLaunchByIdUseCase(launchId)

            when (result) {
                is Result.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            launch = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to load launch details"
                        )
                    }
                }
                is Result.Loading -> {
                    // This shouldn't happen for a single launch fetch, but handle it gracefully
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
     * Retry loading the launch
     */
    fun retry() {
        val launchId = state.value.launch?.id
        if (launchId != null) {
            loadLaunchById(launchId)
        }
    }

    /**
     * Clear any error state
     */
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
} 