package com.jdf.spacexexplorer.domain.model

/**
 * Domain model for a SpaceX core (first stage booster)
 */
data class Core(
    val id: String,
    val serial: String,
    val block: Int?,
    val status: String,
    val reuseCount: Int,
    val rtlsAttempts: Int,
    val rtlsLandings: Int,
    val asdsAttempts: Int,
    val asdsLandings: Int,
    val lastUpdate: String?,
    val launches: List<String>
) 