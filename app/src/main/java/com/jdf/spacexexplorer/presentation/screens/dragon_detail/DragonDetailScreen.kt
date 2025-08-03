package com.jdf.spacexexplorer.presentation.screens.dragon_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
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
fun DragonDetailScreen(
    navController: NavController,
    viewModel: DragonDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = state.dragon?.name ?: "Dragon Details",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (state.hasContent) {
                        IconButton(onClick = { viewModel.retry() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isInitialLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
            state.hasError -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorMessage(
                        message = state.error ?: "Unknown error occurred",
                        onRetry = { viewModel.retry() }
                    )
                }
            }
            state.hasContent -> {
                val dragon = state.dragon!!
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
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
                                dragon.flickrImages.firstOrNull()?.let { imageUrl ->
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = "Image of ${dragon.name}",
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

                    // Basic Information Section
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
                                    text = "Basic Information",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                
                                InfoRow("Name", dragon.name)
                                InfoRow("Type", dragon.type)
                                InfoRow("Status", if (dragon.active) "Active" else "Inactive")
                                InfoRow("Crew Capacity", "${dragon.crewCapacity} astronauts")
                                dragon.firstFlight?.let { InfoRow("First Flight", it) }
                                dragon.description?.let { InfoRow("Description", it) }
                            }
                        }
                    }

                    // Technical Specifications Section
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
                                    text = "Technical Specifications",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                dragon.dryMassKg?.let { InfoRow("Dry Mass", "${String.format(java.util.Locale.US, "%,.0f", it)} kg") }
                                dragon.dryMassLbs?.let { InfoRow("Dry Mass", "${String.format(java.util.Locale.US, "%,.0f", it)} lbs") }
                                                                       dragon.heightWTrunk?.let { InfoRow("Height with Trunk", "${it.meters}m (${it.feet}ft)") }
                                dragon.diameter?.let { InfoRow("Diameter", "${it.meters}m (${it.feet}ft)") }
                                dragon.sidewallAngleDeg?.let { InfoRow("Sidewall Angle", "${it}°") }
                                dragon.orbitDurationYr?.let { InfoRow("Orbit Duration", "${it} years") }
                            }
                        }
                    }

                    // Heat Shield Section
                    dragon.heatShield?.let { heatShield ->
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
                                        text = "Heat Shield",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    
                                    InfoRow("Material", heatShield.material)
                                    InfoRow("Size", "${heatShield.sizeMeters}m")
                                    InfoRow("Temperature", "${heatShield.tempDegrees}°C")
                                    InfoRow("Development Partner", heatShield.devPartner)
                                }
                            }
                        }
                    }

                    // Thrusters Section
                    if (dragon.thrusters.isNotEmpty()) {
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
                                        text = "Thrusters",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    
                                    dragon.thrusters.forEachIndexed { index, thruster ->
                                        val thrusterNumber = index + 1
                                        InfoRow("Thruster $thrusterNumber Type", thruster.type)
                                        InfoRow("Thruster $thrusterNumber Amount", thruster.amount.toString())
                                        InfoRow("Thruster $thrusterNumber Pods", thruster.pods.toString())
                                        InfoRow("Thruster $thrusterNumber Fuel", "${thruster.fuel1}, ${thruster.fuel2}")
                                        InfoRow("Thruster $thrusterNumber ISP", "${thruster.isp}s")
                                        InfoRow("Thruster $thrusterNumber Thrust", "${thruster.thrust.kN} kN (${thruster.thrust.lbf} lbf)")
                                        if (index < dragon.thrusters.size - 1) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Payload Information Section
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
                                    text = "Payload Information",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                
                                dragon.launchPayloadMass?.let { mass ->
                                    mass.kg?.let { InfoRow("Launch Payload Mass", "${String.format(java.util.Locale.US, "%,.0f", it)} kg") }
                                    mass.lb?.let { InfoRow("Launch Payload Mass", "${String.format(java.util.Locale.US, "%,.0f", it)} lbs") }
                                }
                                
                                dragon.returnPayloadMass?.let { mass ->
                                    mass.kg?.let { InfoRow("Return Payload Mass", "${String.format(java.util.Locale.US, "%,.0f", it)} kg") }
                                    mass.lb?.let { InfoRow("Return Payload Mass", "${String.format(java.util.Locale.US, "%,.0f", it)} lbs") }
                                }
                                
                                dragon.pressurizedCapsule?.let { capsule ->
                                    InfoRow("Pressurized Capsule Volume", "${capsule.payloadVolume.cubicMeters}m³ (${capsule.payloadVolume.cubicFeet}ft³)")
                                }
                                
                                dragon.trunk?.let { trunk ->
                                    InfoRow("Trunk Volume", "${trunk.trunkVolume.cubicMeters}m³ (${trunk.trunkVolume.cubicFeet}ft³)")
                                    InfoRow("Solar Arrays", trunk.cargo.solarArray.toString())
                                    InfoRow("Unpressurized Cargo", if (trunk.cargo.unpressurizedCargo) "Yes" else "No")
                                }
                            }
                        }
                    }

                    // Image Gallery Section
                    if (dragon.flickrImages.size > 1) {
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
                                        text = "Gallery",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(dragon.flickrImages.drop(1)) { imageUrl ->
                                            Card(
                                                modifier = Modifier
                                                    .width(120.dp)
                                                    .height(80.dp),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                AsyncImage(
                                                    model = imageUrl,
                                                    contentDescription = "Gallery image of ${dragon.name}",
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Wikipedia Link Section
                    dragon.wikipedia?.let { url ->
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
                                        onClick = { uriHandler.openUri(url) },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text(
                                            text = "View on Wikipedia",
                                            fontWeight = FontWeight.Medium
                                        )
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End
        )
    }
} 