package com.jdf.spacexexplorer.domain.model

/**
 * Domain model representing a SpaceX rocket with clean, essential properties
 * needed by the UI. This is the single source of truth for the presentation layer.
 */
data class Rocket(
    val id: String,
    val name: String,
    val type: String,
    val description: String?,
    val height: Double?,
    val mass: Double?,
    val active: Boolean,
    val stages: Int,
    val boosters: Int,
    val costPerLaunch: Long?,
    val successRatePct: Int?,
    val firstFlight: String?,
    val country: String?,
    val company: String?,
    val wikipediaUrl: String?,
    val flickrImages: List<String>
) 