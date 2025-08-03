package com.jdf.spacexexplorer.presentation.screens.ship_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetShipByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the ship detail screen
 */
@HiltViewModel
class ShipDetailViewModel @Inject constructor(
    private val getShipByIdUseCase: GetShipByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ShipDetailState())
    val state: StateFlow<ShipDetailState> = _state.asStateFlow()

    fun onEvent(event: ShipDetailEvent) {
        when (event) {
            is ShipDetailEvent.LoadShip -> loadShip(event.id)
            is ShipDetailEvent.DismissError -> dismissError()
        }
    }

    private fun loadShip(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = getShipByIdUseCase(id)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            ship = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                }
                is Result.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
} 