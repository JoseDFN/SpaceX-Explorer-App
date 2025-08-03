package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DragonDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "active") val active: Boolean,
    @field:Json(name = "crew_capacity") val crewCapacity: Int,
    @field:Json(name = "sidewall_angle_deg") val sidewallAngleDeg: Double?,
    @field:Json(name = "orbit_duration_yr") val orbitDurationYr: Int?,
    @field:Json(name = "dry_mass_kg") val dryMassKg: Double?,
    @field:Json(name = "dry_mass_lbs") val dryMassLbs: Double?,
    @field:Json(name = "first_flight") val firstFlight: String?,
    @field:Json(name = "heat_shield") val heatShield: HeatShieldDto?,
    @field:Json(name = "thrusters") val thrusters: List<ThrusterDto>,
    @field:Json(name = "launch_payload_mass") val launchPayloadMass: MassDto?,
    @field:Json(name = "return_payload_mass") val returnPayloadMass: MassDto?,
    @field:Json(name = "pressurized_capsule") val pressurizedCapsule: PressurizedCapsuleDto?,
    @field:Json(name = "trunk") val trunk: TrunkDto?,
    @field:Json(name = "height_w_trunk") val heightWTrunk: Double?,
    @field:Json(name = "diameter") val diameter: DiameterDto?,
    @field:Json(name = "flickr_images") val flickrImages: List<String>,
    @field:Json(name = "wikipedia") val wikipedia: String?,
    @field:Json(name = "description") val description: String?
)

@JsonClass(generateAdapter = true)
data class HeatShieldDto(
    @field:Json(name = "material") val material: String,
    @field:Json(name = "size_meters") val sizeMeters: Double,
    @field:Json(name = "temp_degrees") val tempDegrees: Int,
    @field:Json(name = "dev_partner") val devPartner: String
)

@JsonClass(generateAdapter = true)
data class ThrusterDto(
    @field:Json(name = "type") val type: String,
    @field:Json(name = "amount") val amount: Int,
    @field:Json(name = "pods") val pods: Int,
    @field:Json(name = "fuel_1") val fuel1: String,
    @field:Json(name = "fuel_2") val fuel2: String,
    @field:Json(name = "isp") val isp: Int,
    @field:Json(name = "thrust") val thrust: ThrustDto
)

@JsonClass(generateAdapter = true)
data class ThrustDto(
    @field:Json(name = "kN") val kN: Double,
    @field:Json(name = "lbf") val lbf: Double
)



@JsonClass(generateAdapter = true)
data class PressurizedCapsuleDto(
    @field:Json(name = "payload_volume") val payloadVolume: PayloadVolumeDto
)

@JsonClass(generateAdapter = true)
data class PayloadVolumeDto(
    @field:Json(name = "cubic_meters") val cubicMeters: Double,
    @field:Json(name = "cubic_feet") val cubicFeet: Double
)

@JsonClass(generateAdapter = true)
data class TrunkDto(
    @field:Json(name = "trunk_volume") val trunkVolume: PayloadVolumeDto,
    @field:Json(name = "cargo") val cargo: CargoDto
)

@JsonClass(generateAdapter = true)
data class CargoDto(
    @field:Json(name = "solar_array") val solarArray: Int,
    @field:Json(name = "unpressurized_cargo") val unpressurizedCargo: Boolean
)

@JsonClass(generateAdapter = true)
data class DiameterDto(
    @field:Json(name = "meters") val meters: Double,
    @field:Json(name = "feet") val feet: Double
) 