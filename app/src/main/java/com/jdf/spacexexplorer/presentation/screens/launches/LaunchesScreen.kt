package com.jdf.spacexexplorer.presentation.screens.launches

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jdf.spacexexplorer.presentation.components.LaunchCard
import com.jdf.spacexexplorer.presentation.components.LoadingIndicator
import com.jdf.spacexexplorer.presentation.components.ErrorMessage

/**
 * Main screen composable for displaying the launches list.
 */
@Composable
fun LaunchesScreen() {
    val viewModel: LaunchesViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> LoadingIndicator()
        state.error != null -> ErrorMessage(message = state.error ?: "Unknown error")
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.launches) { launch ->
                LaunchCard(launch = launch)
            }
        }
    }
} 