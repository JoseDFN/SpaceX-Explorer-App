package com.jdf.spacexexplorer.presentation.screens.dragon_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetDragonByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Dragon Detail screen
 */
@HiltViewModel
class DragonDetailViewModel @Inject constructor(
    private val getDragonByIdUseCase: GetDragonByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DragonDetailState())
    val state: StateFlow<DragonDetailState> = _state.asStateFlow()

    init {
        // Retrieve dragon ID from navigation arguments
        val dragonId = savedStateHandle.get<String>("dragonId")
        if (dragonId != null) {
            loadDragon(dragonId)
        } else {
            _state.update { it.copy(error = "Dragon ID not provided") }
        }
    }

    /**
     * Load dragon data by ID
     */
    private fun loadDragon(dragonId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getDragonByIdUseCase(dragonId)) {
                is Result.Success -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            dragon = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to load dragon details"
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
     * Retry loading the dragon
     */
    fun retry() {
        val dragonId = state.value.dragon?.id
        if (dragonId != null) {
            loadDragon(dragonId)
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
} 