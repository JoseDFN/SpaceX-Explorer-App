package com.jdf.spacexexplorer.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _searchQueryFlow = MutableStateFlow("")

    init {
        // Set up debounced search
        _searchQueryFlow
            .debounce(400) // 400ms debounce
            .onEach { query ->
                if (query.isNotBlank()) {
                    performSearch(query)
                } else {
                    _state.update { it.copy(results = emptyList(), isLoading = false, error = null) }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Update the search query and trigger debounced search
     */
    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        _searchQueryFlow.value = query
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = searchUseCase(query)) {
                is Result.Success -> {
                    _state.update { 
                        it.copy(
                            results = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Search failed"
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
}
