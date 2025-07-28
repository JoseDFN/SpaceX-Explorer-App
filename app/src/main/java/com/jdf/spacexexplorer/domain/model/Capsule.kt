package com.jdf.spacexexplorer.domain.model

/**
 * Domain model for a SpaceX capsule
 */
data class Capsule(
    val id: String,
    val serial: String,
    val type: String,
    val status: String,
    val reuseCount: Int,
    val waterLandings: Int,
    val landLandings: Int,
    val lastUpdate: String?,
    val launches: List<String>,
    val dragonType: String?
) 