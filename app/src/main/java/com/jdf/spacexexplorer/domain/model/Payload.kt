package com.jdf.spacexexplorer.domain.model

/**
 * Domain model representing a SpaceX payload with clean, essential properties
 * needed by the UI. This is the single source of truth for the presentation layer.
 */
data class Payload(
    val id: String,
    val name: String,
    val type: String,
    val mass: Double?, // in kg
    val orbit: String?,
    val customers: List<String>,
    val nationalities: List<String>,
    val manufacturers: List<String>,
    val payloadMass: Double?, // in kg
    val payloadMassLbs: Double?, // in lbs
    val orbitParams: OrbitParams?,
    val reused: Boolean,
    val launch: String?, // Launch ID
    val dragon: DragonPayload?
)

data class OrbitParams(
    val referenceSystem: String?,
    val regime: String?,
    val longitude: Double?,
    val semiMajorAxisKm: Double?,
    val eccentricity: Double?,
    val periapsisKm: Double?,
    val apoapsisKm: Double?,
    val inclinationDeg: Double?,
    val periodMin: Double?,
    val lifespanYears: Double?,
    val epoch: String?,
    val meanMotion: Double?,
    val raan: Double?,
    val argOfPericenter: Double?,
    val meanAnomaly: Double?
)

data class DragonPayload(
    val capsule: String?,
    val massReturnedKg: Double?,
    val massReturnedLbs: Double?,
    val flightTimeSec: Int?,
    val manifest: String?,
    val waterLanding: Boolean?,
    val landLanding: Boolean?
) 