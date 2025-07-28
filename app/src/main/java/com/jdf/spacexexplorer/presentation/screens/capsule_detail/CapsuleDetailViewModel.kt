package com.jdf.spacexexplorer.presentation.screens.capsule_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetCapsuleByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Capsule Detail screen
 */
@HiltViewModel
class CapsuleDetailViewModel @Inject constructor(
    private val getCapsuleByIdUseCase: GetCapsuleByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(CapsuleDetailState())
    val state: StateFlow<CapsuleDetailState> = _state.asStateFlow()

    init {
        // Retrieve capsule ID from navigation arguments
        val capsuleId = savedStateHandle["capsuleId"]
        if (capsuleId != null) {
            loadCapsule(capsuleId)
        } else {
            _state.update { it.copy(error = "Capsule ID not provided") }
        }
    }

    /**
     * Load capsule data by ID
     */
    private fun loadCapsule(capsuleId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getCapsuleByIdUseCase(capsuleId)) {
                is Result.Success -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            capsule = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to load capsule details"
                        )
                    }
                }
            }
        }
    }

    /**
     * Retry loading the capsule
     */
    fun retry() {
        val capsuleId = state.value.capsule?.id
        if (capsuleId != null) {
            loadCapsule(capsuleId)
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _state.update { it.copy(error = null) }
    }
} 