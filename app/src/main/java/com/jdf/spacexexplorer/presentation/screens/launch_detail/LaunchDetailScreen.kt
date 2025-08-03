package com.jdf.spacexexplorer.presentation.screens.launch_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdf.spacexexplorer.presentation.components.ErrorMessage
import com.jdf.spacexexplorer.presentation.components.LoadingIndicator
import java.text.SimpleDateFormat
import java.util.*

/**
 * Format launch date from ISO string to readable format
 */
private fun formatLaunchDate(dateString: String): String {
    // Check if the string contains an ID concatenated to it
    val cleanDateString = if (dateString.contains("5e9d0d95eda69973a809d")) {
        // Remove the ID part
        dateString.substringBefore("5e9d0d95eda69973a809d")
    } else {
        dateString
    }
    
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(cleanDateString)
        
        val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm UTC", Locale.US)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        // Fallback: try without milliseconds
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(cleanDateString)
            
            val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm UTC", Locale.US)
            outputFormat.format(date ?: Date())
        } catch (e2: Exception) {
            // If all parsing fails, return the cleaned string
            cleanDateString
        }
    }
}

/**
 * Main screen composable for displaying launch details.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailScreen(
    navController: NavController,
    viewModel: LaunchDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Launch Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isInLoadingState -> LoadingIndicator()
                state.hasError -> ErrorMessage(message = state.error ?: "Unknown error")
                state.hasContent -> {
                    state.launch?.let { launch ->
                        LaunchDetailContent(launch = launch)
                    }
                }
            }
        }
    }
}

/**
 * Content composable for displaying launch details.
 */
@Composable
private fun LaunchDetailContent(
    launch: com.jdf.spacexexplorer.domain.model.Launch,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mission Header
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = launch.missionName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // Launch Status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val statusColor = when {
                        launch.isUpcoming -> MaterialTheme.colorScheme.primary
                        launch.wasSuccessful == true -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    }
                    
                    val statusText = when {
                        launch.isUpcoming -> "Upcoming"
                        launch.wasSuccessful == true -> "Successful"
                        else -> "Failed"
                    }
                    
                    Surface(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = statusText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = statusColor
                        )
                    }
                }
            }
        }

        // Launch Details
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Launch Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                DetailRow("Flight Number", launch.flightNumber.toString())
                DetailRow("Launch Date", formatLaunchDate(launch.launchDate))
                DetailRow("Rocket ID", launch.rocketId)
                
                launch.details?.let { details ->
                    DetailRow("Details", details)
                }
            }
        }

        // Links Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Links",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                launch.webcastUrl?.let { url ->
                    DetailRow("Webcast", url)
                }
                
                launch.articleUrl?.let { url ->
                    DetailRow("Article", url)
                }
                
                launch.wikipediaUrl?.let { url ->
                    DetailRow("Wikipedia", url)
                }
            }
        }
    }
}

/**
 * Reusable composable for displaying a detail row.
 */
@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 