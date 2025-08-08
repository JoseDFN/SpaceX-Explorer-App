package com.jdf.spacexexplorer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jdf.spacexexplorer.domain.model.FilterOption

/**
 * A generic filter event that can be used across all screens
 */
sealed class FilterEvent {
    data class UpdateFilter(val filter: FilterOption) : FilterEvent()
    data class RemoveFilter(val filterKey: String) : FilterEvent()
    object ClearAllFilters : FilterEvent()
}

/**
 * A reusable component for displaying and managing filters.
 * This component dynamically renders different UI controls based on the filter type.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBar(
    filters: List<FilterOption>,
    activeFilters: Map<String, FilterOption> = emptyMap(),
    onEvent: (FilterEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Filter Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter Button
                TextButton(
                    onClick = { showFilterMenu = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Filter")
                }
                
                // Clear All Filters Button (only show if there are active filters)
                if (activeFilters.isNotEmpty()) {
                    TextButton(
                        onClick = { onEvent(FilterEvent.ClearAllFilters) }
                    ) {
                        Text("Clear All")
                    }
                }
            }
            
            // Active Filters Display
            if (activeFilters.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(activeFilters.toList()) { (key, filter) ->
                        ActiveFilterChip(
                            filter = filter,
                            onRemove = { onEvent(FilterEvent.RemoveFilter(key)) }
                        )
                    }
                }
            }
            
            // Filter Checkbox Menu
            if (showFilterMenu) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Select Filters",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                filters.forEach { filter ->
                            val isActive = activeFilters.values.any { activeFilter ->
                                when (activeFilter) {
                                    is FilterOption.LaunchYearFilter -> filter is FilterOption.LaunchYearFilter && activeFilter.year == filter.year
                                    is FilterOption.LaunchSuccessFilter -> filter is FilterOption.LaunchSuccessFilter && activeFilter.successful == filter.successful
                                    is FilterOption.LaunchUpcomingFilter -> filter is FilterOption.LaunchUpcomingFilter && activeFilter.upcoming == filter.upcoming
                                    is FilterOption.LaunchRocketFilter -> filter is FilterOption.LaunchRocketFilter && activeFilter.rocketId == filter.rocketId
                                    is FilterOption.LaunchDateRangeFilter -> filter is FilterOption.LaunchDateRangeFilter && activeFilter.startDate == filter.startDate && activeFilter.endDate == filter.endDate
                                    is FilterOption.RocketActiveFilter -> filter is FilterOption.RocketActiveFilter && activeFilter.active == filter.active
                                    is FilterOption.RocketTypeFilter -> filter is FilterOption.RocketTypeFilter && activeFilter.type == filter.type
                                    is FilterOption.CrewAgencyFilter -> filter is FilterOption.CrewAgencyFilter && activeFilter.agency == filter.agency
                                    is FilterOption.CrewStatusFilter -> filter is FilterOption.CrewStatusFilter && activeFilter.status == filter.status
                                    is FilterOption.CapsuleTypeFilter -> filter is FilterOption.CapsuleTypeFilter && activeFilter.type == filter.type
                                    is FilterOption.CapsuleStatusFilter -> filter is FilterOption.CapsuleStatusFilter && activeFilter.status == filter.status
                                    is FilterOption.CoreStatusFilter -> filter is FilterOption.CoreStatusFilter && activeFilter.status == filter.status
                                    is FilterOption.CoreBlockFilter -> filter is FilterOption.CoreBlockFilter && activeFilter.block == filter.block
                                    is FilterOption.DragonTypeFilter -> filter is FilterOption.DragonTypeFilter && activeFilter.type == filter.type
                                    is FilterOption.DragonActiveFilter -> filter is FilterOption.DragonActiveFilter && activeFilter.active == filter.active
                                    is FilterOption.ShipActiveFilter -> filter is FilterOption.ShipActiveFilter && activeFilter.active == filter.active
                                    is FilterOption.ShipTypeFilter -> filter is FilterOption.ShipTypeFilter && activeFilter.type == filter.type
                                    is FilterOption.LandpadStatusFilter -> filter is FilterOption.LandpadStatusFilter && activeFilter.status == filter.status
                                    is FilterOption.LandpadTypeFilter -> filter is FilterOption.LandpadTypeFilter && activeFilter.type == filter.type
                                    is FilterOption.LaunchpadStatusFilter -> filter is FilterOption.LaunchpadStatusFilter && activeFilter.status == filter.status
                                    is FilterOption.LaunchpadRegionFilter -> filter is FilterOption.LaunchpadRegionFilter && activeFilter.region == filter.region
                                    is FilterOption.PayloadTypeFilter -> filter is FilterOption.PayloadTypeFilter && activeFilter.type == filter.type
                                    is FilterOption.PayloadNationalityFilter -> filter is FilterOption.PayloadNationalityFilter && activeFilter.nationality == filter.nationality
                                }
                            }
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isActive,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            onEvent(FilterEvent.UpdateFilter(filter))
                                        } else {
                                            // Find the key for this filter and remove it
                                            val keyToRemove = activeFilters.entries.find { (_, activeFilter) ->
                                                when (activeFilter) {
                                                    is FilterOption.LaunchYearFilter -> filter is FilterOption.LaunchYearFilter && activeFilter.year == filter.year
                                                    is FilterOption.LaunchSuccessFilter -> filter is FilterOption.LaunchSuccessFilter && activeFilter.successful == filter.successful
                                                    is FilterOption.LaunchUpcomingFilter -> filter is FilterOption.LaunchUpcomingFilter && activeFilter.upcoming == filter.upcoming
                                                    is FilterOption.LaunchRocketFilter -> filter is FilterOption.LaunchRocketFilter && activeFilter.rocketId == filter.rocketId
                                                    is FilterOption.LaunchDateRangeFilter -> filter is FilterOption.LaunchDateRangeFilter && activeFilter.startDate == filter.startDate && activeFilter.endDate == filter.endDate
                                                    is FilterOption.RocketActiveFilter -> filter is FilterOption.RocketActiveFilter && activeFilter.active == filter.active
                                                    is FilterOption.RocketTypeFilter -> filter is FilterOption.RocketTypeFilter && activeFilter.type == filter.type
                                                    is FilterOption.CrewAgencyFilter -> filter is FilterOption.CrewAgencyFilter && activeFilter.agency == filter.agency
                                                    is FilterOption.CrewStatusFilter -> filter is FilterOption.CrewStatusFilter && activeFilter.status == filter.status
                                                    is FilterOption.CapsuleTypeFilter -> filter is FilterOption.CapsuleTypeFilter && activeFilter.type == filter.type
                                                    is FilterOption.CapsuleStatusFilter -> filter is FilterOption.CapsuleStatusFilter && activeFilter.status == filter.status
                                                    is FilterOption.CoreStatusFilter -> filter is FilterOption.CoreStatusFilter && activeFilter.status == filter.status
                                                    is FilterOption.CoreBlockFilter -> filter is FilterOption.CoreBlockFilter && activeFilter.block == filter.block
                                                    is FilterOption.DragonTypeFilter -> filter is FilterOption.DragonTypeFilter && activeFilter.type == filter.type
                                                    is FilterOption.DragonActiveFilter -> filter is FilterOption.DragonActiveFilter && activeFilter.active == filter.active
                                                    is FilterOption.ShipActiveFilter -> filter is FilterOption.ShipActiveFilter && activeFilter.active == filter.active
                                                    is FilterOption.ShipTypeFilter -> filter is FilterOption.ShipTypeFilter && activeFilter.type == filter.type
                                                    is FilterOption.LandpadStatusFilter -> filter is FilterOption.LandpadStatusFilter && activeFilter.status == filter.status
                                                    is FilterOption.LandpadTypeFilter -> filter is FilterOption.LandpadTypeFilter && activeFilter.type == filter.type
                                                    is FilterOption.LaunchpadStatusFilter -> filter is FilterOption.LaunchpadStatusFilter && activeFilter.status == filter.status
                                                    is FilterOption.LaunchpadRegionFilter -> filter is FilterOption.LaunchpadRegionFilter && activeFilter.region == filter.region
                                                    is FilterOption.PayloadTypeFilter -> filter is FilterOption.PayloadTypeFilter && activeFilter.type == filter.type
                                                    is FilterOption.PayloadNationalityFilter -> filter is FilterOption.PayloadNationalityFilter && activeFilter.nationality == filter.nationality
                                                }
                                            }?.key
                                            keyToRemove?.let { onEvent(FilterEvent.RemoveFilter(it)) }
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                        uncheckedColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                                
                            Text(
                                text = getFilterDisplayName(filter),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { showFilterMenu = false }
                            ) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }
}



/**
 * Get a user-friendly display name for a filter option.
 */
private fun getFilterDisplayName(filter: FilterOption): String {
    return when (filter) {
        is FilterOption.LaunchYearFilter -> "Year: ${filter.year}"
        is FilterOption.LaunchSuccessFilter -> "Success: ${if (filter.successful) "Yes" else "No"}"
        is FilterOption.LaunchUpcomingFilter -> "Upcoming: ${if (filter.upcoming) "Yes" else "No"}"
        is FilterOption.LaunchRocketFilter -> "Rocket: ${filter.rocketId}"
        is FilterOption.LaunchDateRangeFilter -> "Date Range"
        is FilterOption.RocketActiveFilter -> "Active: ${if (filter.active) "Yes" else "No"}"
        is FilterOption.RocketTypeFilter -> "Type: ${filter.type}"
        is FilterOption.CrewAgencyFilter -> "Agency: ${filter.agency}"
        is FilterOption.CrewStatusFilter -> "Status: ${filter.status}"
        is FilterOption.CapsuleTypeFilter -> "Type: ${filter.type}"
        is FilterOption.CapsuleStatusFilter -> "Status: ${filter.status}"
        is FilterOption.CoreStatusFilter -> "Status: ${filter.status}"
        is FilterOption.CoreBlockFilter -> "Block: ${filter.block ?: "Unknown"}"
        is FilterOption.DragonTypeFilter -> "Type: ${filter.type}"
        is FilterOption.DragonActiveFilter -> "Active: ${if (filter.active) "Yes" else "No"}"
        is FilterOption.ShipActiveFilter -> "Active: ${if (filter.active) "Yes" else "No"}"
        is FilterOption.ShipTypeFilter -> "Type: ${filter.type}"
        is FilterOption.LandpadStatusFilter -> "Status: ${filter.status}"
        is FilterOption.LandpadTypeFilter -> "Type: ${filter.type}"
        is FilterOption.LaunchpadStatusFilter -> "Status: ${filter.status}"
        is FilterOption.LaunchpadRegionFilter -> "Region: ${filter.region}"
        is FilterOption.PayloadTypeFilter -> "Type: ${filter.type}"
        is FilterOption.PayloadNationalityFilter -> "Nationality: ${filter.nationality}"
    }
}

/**
 * A chip component to display an active filter with a remove button.
 */
@Composable
private fun ActiveFilterChip(
    filter: FilterOption,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getFilterDisplayName(filter),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Remove filter",
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
} 