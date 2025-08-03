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
import com.jdf.spacexexplorer.domain.model.Core

@Composable
fun CoreCard(
    core: Core,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with Serial and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = core.serial,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                val statusColor = when (core.status.lowercase()) {
                    "active" -> Color.Green
                    "unknown" -> Color.Gray
                    "inactive" -> Color.Red
                    else -> MaterialTheme.colorScheme.primary
                }
                
                Text(
                    text = core.status.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Block information
            core.block?.let { block ->
                Text(
                    text = "Block $block",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            
            // Statistics Row - Reuse and Landing Attempts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("Reuses", core.reuseCount.toString())
                StatItem("RTLS Attempts", core.rtlsAttempts.toString())
                StatItem("ASDS Attempts", core.asdsAttempts.toString())
            }
            
            // Landing Success Statistics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("RTLS Landings", core.rtlsLandings.toString())
                StatItem("ASDS Landings", core.asdsLandings.toString())
                StatItem("Success Rate", "${calculateSuccessRate(core)}%")
            }
            
            // Launches count
            if (core.launches.isNotEmpty()) {
                Text(
                    text = "${core.launches.size} launch${if (core.launches.size != 1) "es" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}

private fun calculateSuccessRate(core: Core): Int {
    val totalAttempts = core.rtlsAttempts + core.asdsAttempts
    val totalLandings = core.rtlsLandings + core.asdsLandings
    
    return if (totalAttempts > 0) {
        ((totalLandings.toFloat() / totalAttempts.toFloat()) * 100).toInt()
    } else {
        0
    }
} 