package com.jdf.spacexexplorer.domain.model

/**
 * Domain model for a SpaceX Dragon spacecraft
 */
data class Dragon(
    val id: String,
    val name: String,
    val type: String,
    val active: Boolean,
    val crewCapacity: Int,
    val sidewallAngleDeg: Double?,
    val orbitDurationYr: Int?,
    val dryMassKg: Double?,
    val dryMassLbs: Double?,
    val firstFlight: String?,
    val heatShield: HeatShield?,
    val thrusters: List<Thruster>,
    val launchPayloadMass: Mass?,
    val returnPayloadMass: Mass?,
    val pressurizedCapsule: PressurizedCapsule?,
    val trunk: Trunk?,
    val heightWTrunk: Diameter?,
    val diameter: Diameter?,
    val flickrImages: List<String>,
    val wikipedia: String?,
    val description: String?
)

data class HeatShield(
    val material: String,
    val sizeMeters: Double,
    val tempDegrees: Int,
    val devPartner: String
)

data class Thruster(
    val type: String,
    val amount: Int,
    val pods: Int,
    val fuel1: String,
    val fuel2: String,
    val isp: Int,
    val thrust: Thrust
)

data class Thrust(
    val kN: Double,
    val lbf: Double
)

data class Mass(
    val kg: Double?,
    val lb: Double?
)

data class PressurizedCapsule(
    val payloadVolume: PayloadVolume
)

data class PayloadVolume(
    val cubicMeters: Double,
    val cubicFeet: Double
)

data class Trunk(
    val trunkVolume: PayloadVolume,
    val cargo: Cargo
)

data class Cargo(
    val solarArray: Int,
    val unpressurizedCargo: Boolean
)

data class Diameter(
    val meters: Double,
    val feet: Double
) 