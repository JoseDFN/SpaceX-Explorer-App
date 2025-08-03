package com.jdf.spacexexplorer.presentation.screens.crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetCrewUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshCrewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the crew screen
 */
@HiltViewModel
class CrewViewModel @Inject constructor(
    private val getCrewUseCase: GetCrewUseCase,
    private val refreshCrewUseCase: RefreshCrewUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CrewState())
    val state: StateFlow<CrewState> = _state.asStateFlow()

    init {
        loadCrew()
    }

    fun onEvent(event: CrewEvent) {
        when (event) {
            is CrewEvent.LoadCrew -> loadCrew()
            is CrewEvent.RefreshCrew -> refreshCrew()
            is CrewEvent.DismissError -> dismissError()
        }
    }

    private fun loadCrew() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getCrewUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _state.update { 
                            it.copy(
                                crew = result.data,
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

    private fun refreshCrew() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }
            
            when (val result = refreshCrewUseCase()) {
                is Result.Success -> {
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isRefreshing = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                }
                is Result.Loading -> {
                    _state.update { it.copy(isRefreshing = true) }
                }
            }
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
} 