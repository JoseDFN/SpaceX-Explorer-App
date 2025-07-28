package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "capsules")
data class CapsuleEntity(
    @PrimaryKey val id: String,
    val serial: String,
    val type: String,
    val status: String,
    val reuseCount: Int,
    val waterLandings: Int,
    val landLandings: Int,
    val lastUpdate: String?,
    val launches: String, // Stored as JSON string
    val dragonType: String?
) 