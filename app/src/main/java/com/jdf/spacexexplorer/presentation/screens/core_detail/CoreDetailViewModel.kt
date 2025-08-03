package com.jdf.spacexexplorer.presentation.screens.core_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetCoreByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Core Detail screen
 */
@HiltViewModel
class CoreDetailViewModel @Inject constructor(
    private val getCoreByIdUseCase: GetCoreByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(CoreDetailState())
    val state: StateFlow<CoreDetailState> = _state.asStateFlow()

    init {
        // Retrieve core ID from navigation arguments
        val coreId = savedStateHandle.get<String>("coreId")
        if (coreId != null) {
            loadCore(coreId)
        } else {
            _state.update { it.copy(error = "Core ID not provided") }
        }
    }

    /**
     * Load core data by ID
     */
    private fun loadCore(coreId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getCoreByIdUseCase(coreId)) {
                is Result.Success -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            core = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to load core details"
                        )
                    }
                }
                is Result.Loading -> {
                    _state.update { 
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }

            }
        }
    }

    /**
     * Retry loading the core
     */
    fun retry() {
        val coreId = state.value.core?.id
        if (coreId != null) {
            loadCore(coreId)
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
} 