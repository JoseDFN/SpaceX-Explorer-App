package com.jdf.spacexexplorer.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.usecase.ClearCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val clearCacheUseCase: ClearCacheUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun onClearCacheClicked() {
        viewModelScope.launch {
            // Clear caches
            clearCacheUseCase()

            // Show confirmation message
            _state.update { it.copy(cacheClearedMessageVisible = true) }

            // Hide after a few seconds
            launch {
                delay(3000)
                _state.update { it.copy(cacheClearedMessageVisible = false) }
            }
        }
    }
}


