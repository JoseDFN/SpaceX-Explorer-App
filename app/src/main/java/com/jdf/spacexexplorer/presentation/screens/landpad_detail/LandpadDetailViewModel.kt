package com.jdf.spacexexplorer.presentation.screens.landpad_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetLandpadByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Landpad Detail screen
 */
@HiltViewModel
class LandpadDetailViewModel @Inject constructor(
    private val getLandpadByIdUseCase: GetLandpadByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LandpadDetailState())
    val state: StateFlow<LandpadDetailState> = _state.asStateFlow()

    init {
        // Retrieve landpad ID from navigation arguments
        val landpadId = savedStateHandle.get<String>("landpadId")
        if (landpadId != null) {
            loadLandpad(landpadId)
        } else {
            _state.update { it.copy(error = "Landpad ID not provided") }
        }
    }

    /**
     * Load landpad data by ID
     */
    private fun loadLandpad(landpadId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getLandpadByIdUseCase(landpadId)) {
                is Result.Success -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            landpad = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to load landpad details"
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
     * Retry loading the landpad
     */
    fun retry() {
        val landpadId = state.value.landpad?.id
        if (landpadId != null) {
            loadLandpad(landpadId)
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
} 