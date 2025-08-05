package com.jdf.spacexexplorer.presentation.screens.payload_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetPayloadByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Payload Detail screen.
 * Manages the UI state and business logic for displaying payload details.
 */
@HiltViewModel
class PayloadDetailViewModel @Inject constructor(
    private val getPayloadByIdUseCase: GetPayloadByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(PayloadDetailState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<PayloadDetailState> = _state.asStateFlow()
    
    init {
        // Get payload ID from navigation arguments
        val payloadId = savedStateHandle["payloadId"] ?: ""
        if (payloadId.isNotEmpty()) {
            loadPayload(payloadId)
        }
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: PayloadDetailEvent) {
        when (event) {
            is PayloadDetailEvent.Retry -> {
                retry()
            }
            is PayloadDetailEvent.DismissError -> {
                clearError()
            }
        }
    }
    
    /**
     * Load payload from the use case
     */
    private fun loadPayload(payloadId: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }
            
            val result = getPayloadByIdUseCase(payloadId)
            
            when (result) {
                is Result.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            payload = result.data,
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
                    // This shouldn't happen for a single payload fetch, but handle it gracefully
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
     * Retry loading payload
     */
    private fun retry() {
        val payloadId = state.value.payload?.id ?: return
        clearError()
        loadPayload(payloadId)
    }
} 