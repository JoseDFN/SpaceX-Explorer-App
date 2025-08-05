package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PayloadDto(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "type")
    val type: String,
    @field:Json(name = "mass_kg")
    val mass: Double?,
    @field:Json(name = "orbit")
    val orbit: String?,
    @field:Json(name = "customers")
    val customers: List<String>,
    @field:Json(name = "nationalities")
    val nationalities: List<String>,
    @field:Json(name = "manufacturers")
    val manufacturers: List<String>,
    @field:Json(name = "payload_mass_kg")
    val payloadMass: Double?,
    @field:Json(name = "payload_mass_lbs")
    val payloadMassLbs: Double?,
    @field:Json(name = "orbit_params")
    val orbitParams: OrbitParamsDto?,
    @field:Json(name = "reused")
    val reused: Boolean,
    @field:Json(name = "launch")
    val launch: String?,
    @field:Json(name = "dragon")
    val dragon: DragonPayloadDto?
)

@JsonClass(generateAdapter = true)
data class OrbitParamsDto(
    @field:Json(name = "reference_system")
    val referenceSystem: String?,
    @field:Json(name = "regime")
    val regime: String?,
    @field:Json(name = "longitude")
    val longitude: Double?,
    @field:Json(name = "semi_major_axis_km")
    val semiMajorAxisKm: Double?,
    @field:Json(name = "eccentricity")
    val eccentricity: Double?,
    @field:Json(name = "periapsis_km")
    val periapsisKm: Double?,
    @field:Json(name = "apoapsis_km")
    val apoapsisKm: Double?,
    @field:Json(name = "inclination_deg")
    val inclinationDeg: Double?,
    @field:Json(name = "period_min")
    val periodMin: Double?,
    @field:Json(name = "lifespan_years")
    val lifespanYears: Double?,
    @field:Json(name = "epoch")
    val epoch: String?,
    @field:Json(name = "mean_motion")
    val meanMotion: Double?,
    @field:Json(name = "raan")
    val raan: Double?,
    @field:Json(name = "arg_of_pericenter")
    val argOfPericenter: Double?,
    @field:Json(name = "mean_anomaly")
    val meanAnomaly: Double?
)

@JsonClass(generateAdapter = true)
data class DragonPayloadDto(
    @field:Json(name = "capsule")
    val capsule: String?,
    @field:Json(name = "mass_returned_kg")
    val massReturnedKg: Double?,
    @field:Json(name = "mass_returned_lbs")
    val massReturnedLbs: Double?,
    @field:Json(name = "flight_time_sec")
    val flightTimeSec: Int?,
    @field:Json(name = "manifest")
    val manifest: String?,
    @field:Json(name = "water_landing")
    val waterLanding: Boolean?,
    @field:Json(name = "land_landing")
    val landLanding: Boolean?
) 