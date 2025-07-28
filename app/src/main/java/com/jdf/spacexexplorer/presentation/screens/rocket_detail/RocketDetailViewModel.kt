package com.jdf.spacexexplorer.presentation.screens.rocket_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetRocketByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Rocket Detail screen
 */
@HiltViewModel
class RocketDetailViewModel @Inject constructor(
    private val getRocketByIdUseCase: GetRocketByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(RocketDetailState())
    val state: StateFlow<RocketDetailState> = _state.asStateFlow()

    init {
        // Retrieve rocket ID from navigation arguments
        val rocketId = savedStateHandle.get<String>("rocketId")
        if (rocketId != null) {
            loadRocket(rocketId)
        } else {
            _state.update { it.copy(error = "Rocket ID not provided") }
        }
    }

    /**
     * Load rocket data by ID
     */
    private fun loadRocket(rocketId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getRocketByIdUseCase(rocketId)) {
                is Result.Success -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            rocket = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to load rocket details"
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
     * Retry loading the rocket
     */
    fun retry() {
        val rocketId = state.value.rocket?.id
        if (rocketId != null) {
            loadRocket(rocketId)
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
} 