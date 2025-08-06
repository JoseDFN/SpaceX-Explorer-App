package com.jdf.spacexexplorer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.jdf.spacexexplorer.domain.model.SortOption

/**
 * A reusable component for displaying and managing filters and sorting options.
 * This component can be used across different screens that support filtering and sorting.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSortBar(
    availableFilters: List<FilterOption>,
    activeFilters: Map<String, FilterOption>,
    currentSort: SortOption,
    onFilterUpdate: (FilterOption) -> Unit,
    onFilterRemove: (String) -> Unit,
    onClearAllFilters: () -> Unit,
    onSortUpdate: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    
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
            // Filter and Sort Controls Row
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
                
                // Sort Button
                TextButton(
                    onClick = { showSortMenu = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sort")
                }
                
                // Clear All Filters Button (only show if there are active filters)
                if (activeFilters.isNotEmpty()) {
                    TextButton(
                        onClick = onClearAllFilters
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
                            onRemove = { onFilterRemove(key) }
                        )
                    }
                }
            }
            
            // Dropdown Menus
            DropdownMenu(
                expanded = showFilterMenu,
                onDismissRequest = { showFilterMenu = false }
            ) {
                availableFilters.forEach { filter ->
                    DropdownMenuItem(
                        text = { 
                            Text(
                                text = getFilterDisplayName(filter),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        onClick = {
                            onFilterUpdate(filter)
                            showFilterMenu = false
                        }
                    )
                }
            }
            
            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false }
            ) {
                SortOption.values().forEach { sortOption ->
                    DropdownMenuItem(
                        text = { 
                            Text(
                                text = getSortDisplayName(sortOption),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        onClick = {
                            onSortUpdate(sortOption)
                            showSortMenu = false
                        }
                    )
                }
            }
        }
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
 * Get a user-friendly display name for a sort option.
 */
private fun getSortDisplayName(sort: SortOption): String {
    return when (sort) {
        SortOption.DATE_DESC -> "Date (Newest First)"
        SortOption.DATE_ASC -> "Date (Oldest First)"
        SortOption.NAME_ASC -> "Name (A-Z)"
        SortOption.NAME_DESC -> "Name (Z-A)"
        SortOption.FLIGHT_NUMBER_ASC -> "Flight Number (Low to High)"
        SortOption.FLIGHT_NUMBER_DESC -> "Flight Number (High to Low)"
        SortOption.ROCKET_NAME_ASC -> "Rocket Name (A-Z)"
        SortOption.ROCKET_NAME_DESC -> "Rocket Name (Z-A)"
        SortOption.CREW_NAME_ASC -> "Crew Name (A-Z)"
        SortOption.CREW_NAME_DESC -> "Crew Name (Z-A)"
        SortOption.CAPSULE_SERIAL_ASC -> "Capsule Serial (A-Z)"
        SortOption.CAPSULE_SERIAL_DESC -> "Capsule Serial (Z-A)"
        SortOption.CORE_SERIAL_ASC -> "Core Serial (A-Z)"
        SortOption.CORE_SERIAL_DESC -> "Core Serial (Z-A)"
        SortOption.DRAGON_NAME_ASC -> "Dragon Name (A-Z)"
        SortOption.DRAGON_NAME_DESC -> "Dragon Name (Z-A)"
        SortOption.SHIP_NAME_ASC -> "Ship Name (A-Z)"
        SortOption.SHIP_NAME_DESC -> "Ship Name (Z-A)"
        SortOption.LANDPAD_NAME_ASC -> "Landpad Name (A-Z)"
        SortOption.LANDPAD_NAME_DESC -> "Landpad Name (Z-A)"
        SortOption.LAUNCHPAD_NAME_ASC -> "Launchpad Name (A-Z)"
        SortOption.LAUNCHPAD_NAME_DESC -> "Launchpad Name (Z-A)"
        SortOption.PAYLOAD_NAME_ASC -> "Payload Name (A-Z)"
        SortOption.PAYLOAD_NAME_DESC -> "Payload Name (Z-A)"
    }
} 