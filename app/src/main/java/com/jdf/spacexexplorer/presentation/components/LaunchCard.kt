package com.jdf.spacexexplorer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jdf.spacexexplorer.domain.model.Launch
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
        
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        // Fallback: try without milliseconds
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(cleanDateString)
            
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            outputFormat.format(date ?: Date())
        } catch (e2: Exception) {
            // If all parsing fails, return the cleaned string
            cleanDateString
        }
    }
}

/**
 * Beautiful card component for displaying launch information in the launches list.
 */
@Composable
fun LaunchCard(
    launch: Launch,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mission Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Mission name and flight number
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = launch.missionName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = "Flight #${launch.flightNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                
                // Status badge
                val statusColor = when {
                    launch.isUpcoming -> Color(0xFF2196F3) // Blue
                    launch.wasSuccessful == true -> Color(0xFF4CAF50) // Green
                    else -> Color(0xFFF44336) // Red
                }
                
                val statusText = when {
                    launch.isUpcoming -> "UPCOMING"
                    launch.wasSuccessful == true -> "SUCCESS"
                    else -> "FAILED"
                }
                
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }
            
            // Launch details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Launch date
                Column {
                    Text(
                        text = "Launch Date",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        text = formatLaunchDate(launch.launchDate),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Rocket info
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Rocket",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        text = launch.rocketId,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            // Mission details (if available)
            launch.details?.let { details ->
                if (details.isNotBlank()) {
                    Text(
                        text = details,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
} 