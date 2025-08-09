package com.jdf.spacexexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import com.jdf.spacexexplorer.presentation.navigation.AppShell
import com.jdf.spacexexplorer.presentation.ui.theme.SpaceXExplorerTheme
import com.jdf.spacexexplorer.presentation.shared.SharedViewModel
import com.jdf.spacexexplorer.domain.model.Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedViewModel: SharedViewModel = hiltViewModel()
            // Fallback to LIGHT until settings wire the actual stored theme in
            SpaceXExplorerTheme(theme = Theme.LIGHT) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppShell()
                }
            }
        }
    }
} 