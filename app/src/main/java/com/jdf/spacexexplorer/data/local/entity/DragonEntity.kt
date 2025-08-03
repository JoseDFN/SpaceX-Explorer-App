package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dragons")
data class DragonEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val active: Boolean,
    val crewCapacity: Int,
    val sidewallAngleDeg: Double?,
    val orbitDurationYr: Int?,
    val dryMassKg: Double?,
    val dryMassLbs: Double?,
    val firstFlight: String?,
    val heatShield: String, // Stored as JSON string
    val thrusters: String, // Stored as JSON string
    val launchPayloadMass: String?, // Stored as JSON string
    val returnPayloadMass: String?, // Stored as JSON string
    val pressurizedCapsule: String?, // Stored as JSON string
    val trunk: String?, // Stored as JSON string
    val heightWTrunk: Double?,
    val diameter: String?, // Stored as JSON string
    val flickrImages: String, // Stored as JSON string
    val wikipedia: String?,
    val description: String?
) 