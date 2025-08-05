package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launchpads")
data class LaunchpadEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val fullName: String,
    val locality: String,
    val region: String,
    val latitude: Double?,
    val longitude: Double?,
    val launchAttempts: Int,
    val launchSuccesses: Int,
    val rockets: String, // Stored as JSON string
    val timezone: String,
    val launches: String, // Stored as JSON string
    val status: String,
    val details: String?,
    val images: String? // Stored as JSON string
) 