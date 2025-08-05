package com.jdf.spacexexplorer.domain.model

/**
 * Domain model representing a SpaceX launchpad with clean, essential properties
 * needed by the UI. This is the single source of truth for the presentation layer.
 */
data class Launchpad(
    val id: String,
    val name: String,
    val fullName: String,
    val locality: String,
    val region: String,
    val latitude: Double?,
    val longitude: Double?,
    val launchAttempts: Int,
    val launchSuccesses: Int,
    val rockets: List<String>,
    val timezone: String,
    val launches: List<String>,
    val status: String,
    val details: String?,
    val images: LaunchpadImages?
)

data class LaunchpadImages(
    val large: List<String>
) 