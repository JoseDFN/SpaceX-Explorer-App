package com.jdf.spacexexplorer.domain.model

/**
 * Domain model representing a SpaceX landpad with clean, essential properties
 * needed by the UI. This is the single source of truth for the presentation layer.
 */
data class Landpad(
    val id: String,
    val name: String,
    val fullName: String,
    val type: String,
    val locality: String,
    val region: String,
    val latitude: Double?,
    val longitude: Double?,
    val landingAttempts: Int,
    val landingSuccesses: Int,
    val wikipediaUrl: String?,
    val details: String?,
    val status: String,
    val launches: List<String>
) 