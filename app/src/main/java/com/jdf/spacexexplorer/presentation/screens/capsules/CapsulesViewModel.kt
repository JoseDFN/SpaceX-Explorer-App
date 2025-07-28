package com.jdf.spacexexplorer.presentation.screens.capsules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetCapsulesUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshCapsulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        loadCapsules()
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
                // TODO: Navigate to capsule details screen
                // This will be implemented when we add navigation
            }
        }
    }

    /**
     * Load capsules from the use case
     */
    private fun loadCapsules() {
        viewModelScope.launch {
            getCapsulesUseCase().collect { result ->
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