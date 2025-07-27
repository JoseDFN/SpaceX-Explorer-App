package com.jdf.spacexexplorer.presentation.components

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jdf.spacexexplorer.domain.model.Launch

/**
 * Placeholder composable for displaying a Launch card.
 * Shows only the missionName for now.
 */
@Composable
fun LaunchCard(
    launch: Launch,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Text(text = launch.missionName)
    }
} 