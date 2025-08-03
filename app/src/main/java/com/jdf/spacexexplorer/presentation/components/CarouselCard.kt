package com.jdf.spacexexplorer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket
import java.text.SimpleDateFormat
import java.util.*

/**
 * Card component for displaying items in horizontal carousels.
 * Can be used for both launches and rockets.
 */
@Composable
fun CarouselCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        content()
    }
}

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
 * Check if a launch is actually upcoming based on its date
 */
private fun isActuallyUpcoming(launchDate: String): Boolean {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val launchDateParsed = inputFormat.parse(launchDate) ?: return false
        
        val currentDate = Date()
        launchDateParsed.after(currentDate)
    } catch (e: Exception) {
        // Fallback: try without milliseconds
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val launchDateParsed = inputFormat.parse(launchDate) ?: return false
            
            val currentDate = Date()
            launchDateParsed.after(currentDate)
        } catch (e2: Exception) {
            // If parsing fails, return false
            false
        }
    }
}

/**
 * Carousel card specifically for Launch items
 */
@Composable
fun LaunchCarouselCard(
    launch: Launch,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    CarouselCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background image if available
            launch.patchImageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Mission patch for ${launch.missionName}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                // Fallback background color
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
            }
            
            // Content overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Mission name
                Text(
                    text = launch.missionName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Launch date and status
                Column {
                    Text(
                        text = formatLaunchDate(launch.launchDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    
                    // Status indicator - check if actually upcoming based on date
                    val actuallyUpcoming = isActuallyUpcoming(launch.launchDate)
                    val statusText = when {
                        actuallyUpcoming -> "Upcoming"
                        launch.wasSuccessful == true -> "Successful"
                        launch.wasSuccessful == false -> "Failed"
                        else -> "Past"
                    }
                    
                    val statusColor = when {
                        actuallyUpcoming -> Color.Yellow
                        launch.wasSuccessful == true -> Color.Green
                        launch.wasSuccessful == false -> Color.Red
                        else -> Color.Gray
                    }
                    
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor
                    )
                }
            }
        }
    }
}

/**
 * Carousel card specifically for Rocket items
 */
@Composable
fun RocketCarouselCard(
    rocket: Rocket,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    CarouselCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background image if available
            rocket.flickrImages.firstOrNull()?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Image of ${rocket.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                // Fallback background color
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )
            }
            
            // Content overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Rocket name
                Text(
                    text = rocket.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Rocket details
                Column {
                    Text(
                        text = rocket.type,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    
                    // Active status
                    val statusText = if (rocket.active) "Active" else "Inactive"
                    val statusColor = if (rocket.active) Color.Green else Color.Gray
                    
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor
                    )
                }
            }
        }
    }
} 