package com.jdf.spacexexplorer.presentation.screens.ships

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetShipsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshShipsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the ships list screen
 */
@HiltViewModel
class ShipsViewModel @Inject constructor(
    private val getShipsUseCase: GetShipsUseCase,
    private val refreshShipsUseCase: RefreshShipsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ShipsState())
    val state: StateFlow<ShipsState> = _state.asStateFlow()

    init {
        loadShips()
    }

    fun onEvent(event: ShipsEvent) {
        when (event) {
            is ShipsEvent.LoadShips -> loadShips()
            is ShipsEvent.RefreshShips -> refreshShips()
            is ShipsEvent.DismissError -> dismissError()
        }
    }

    private fun loadShips() {
        viewModelScope.launch {
            getShipsUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                ships = result.data,
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
    }

    private fun refreshShips() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = refreshShipsUseCase()) {
                is Result.Success -> {
                    // The ships will be updated through the Flow from getShipsUseCase
                    _state.update { it.copy(isLoading = false) }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Failed to refresh ships"
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