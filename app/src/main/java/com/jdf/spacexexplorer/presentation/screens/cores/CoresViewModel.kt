package com.jdf.spacexexplorer.presentation.screens.cores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetCoresUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshCoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        loadCores()
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
                // TODO: Navigate to core details screen
                // This will be implemented when we add navigation
            }
        }
    }

    /**
     * Load cores from the use case
     */
    private fun loadCores() {
        viewModelScope.launch {
            getCoresUseCase().collect { result ->
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