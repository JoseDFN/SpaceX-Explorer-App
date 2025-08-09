package com.jdf.spacexexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import com.jdf.spacexexplorer.presentation.navigation.AppShell
import com.jdf.spacexexplorer.presentation.ui.theme.SpaceXExplorerTheme
import com.jdf.spacexexplorer.presentation.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val theme by themeViewModel.theme.collectAsState()

            SpaceXExplorerTheme(theme = theme) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppShell()
                }
            }
        }
    }
} 