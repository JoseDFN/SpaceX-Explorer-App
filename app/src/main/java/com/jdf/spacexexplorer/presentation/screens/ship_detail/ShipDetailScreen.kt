package com.jdf.spacexexplorer.presentation.screens.ship_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jdf.spacexexplorer.presentation.components.ErrorMessage
import com.jdf.spacexexplorer.presentation.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipDetailScreen(
    navController: NavController,
    shipId: String,
    viewModel: ShipDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(shipId) {
        viewModel.onEvent(ShipDetailEvent.LoadShip(shipId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ship Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    LoadingIndicator()
                }
                state.error != null -> {
                    ErrorMessage(
                        message = state.error!!,
                        onRetry = { viewModel.onEvent(ShipDetailEvent.LoadShip(shipId)) }
                    )
                }
                state.ship == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBoat,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ship not found",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    val ship = state.ship!!
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Hero Image Section
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    ship.image?.let { imageUrl ->
                                        AsyncImage(
                                            model = imageUrl,
                                            contentDescription = "Image of ${ship.name}",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } ?: run {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.secondaryContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.DirectionsBoat,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(64.dp),
                                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "No Image Available",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Ship Information Section
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Ship Information",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    
                                    InfoRow("Name", ship.name)
                                    ship.type?.let { InfoRow("Type", it) }
                                    InfoRow("Home Port", ship.homePort)
                                    ship.yearBuilt?.let { InfoRow("Year Built", it.toString()) }
                                    
                                    if (ship.massKg != null || ship.massLbs != null) {
                                        val massText = buildString {
                                            ship.massKg?.let { append("${it} kg") }
                                            if (ship.massKg != null && ship.massLbs != null) append(" / ")
                                            ship.massLbs?.let { append("${it} lbs") }
                                        }
                                        InfoRow("Mass", massText)
                                    }
                                    
                                    if (ship.roles.isNotEmpty()) {
                                        InfoRow("Roles", ship.roles.joinToString(", "))
                                    }
                                    
                                    InfoRow("Launches", "${ship.launches.size} missions")
                                    
                                    // Status
                                    val statusText = if (ship.active) "Active" else "Inactive"
                                    val statusColor = if (ship.active) Color.Green else Color.Gray
                                    InfoRow("Status", statusText, statusColor)
                                }
                            }
                        }

                        // URL link (if available)
                        ship.url?.let { url ->
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "More Information",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        
                                        Button(
                                            onClick = {
                                                uriHandler.openUri(url)
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.Launch,
                                                contentDescription = null
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("View on SpaceX Website")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End
        )
    }
} 