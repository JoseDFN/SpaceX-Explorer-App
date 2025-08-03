package com.jdf.spacexexplorer.domain.model

/**
 * Domain model for a SpaceX ship
 */
data class Ship(
    val id: String,
    val name: String,
    val active: Boolean,
    val launches: List<String>,
    val homePort: String,
    val yearBuilt: Int?,
    val massKg: Double?,
    val massLbs: Double?,
    val type: String?,
    val roles: List<String>,
    val image: String?,
    val url: String?
) 