package com.jdf.spacexexplorer.presentation.screens.crew_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetCrewMemberByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the crew detail screen
 */
@HiltViewModel
class CrewDetailViewModel @Inject constructor(
    private val getCrewMemberByIdUseCase: GetCrewMemberByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CrewDetailState())
    val state: StateFlow<CrewDetailState> = _state.asStateFlow()

    fun onEvent(event: CrewDetailEvent) {
        when (event) {
            is CrewDetailEvent.LoadCrewMember -> loadCrewMember(event.id)
            is CrewDetailEvent.DismissError -> dismissError()
        }
    }

    private fun loadCrewMember(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getCrewMemberByIdUseCase(id)) {
                is Result.Success -> {
                    _state.update { 
                        it.copy(
                            crewMember = result.data,
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