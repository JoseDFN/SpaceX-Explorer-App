package com.jdf.spacexexplorer.presentation.screens.rockets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

/**
 * Placeholder screen for displaying rockets list.
 * This will be implemented in the future.
 */
@Composable
fun RocketsScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Rockets Screen - Coming Soon",
            style = MaterialTheme.typography.headlineMedium
        )
    }
} 