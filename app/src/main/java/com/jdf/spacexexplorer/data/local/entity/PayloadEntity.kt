package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payloads")
data class PayloadEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val mass: Double?, // in kg
    val orbit: String?,
    val customers: String, // Stored as JSON string
    val nationalities: String, // Stored as JSON string
    val manufacturers: String, // Stored as JSON string
    val payloadMass: Double?, // in kg
    val payloadMassLbs: Double?, // in lbs
    val orbitParams: String?, // Stored as JSON string
    val reused: Boolean,
    val launch: String?, // Launch ID
    val dragon: String? // Stored as JSON string
) 