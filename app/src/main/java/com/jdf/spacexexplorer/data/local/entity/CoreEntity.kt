package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cores")
data class CoreEntity(
    @PrimaryKey val id: String,
    val serial: String,
    val block: Int?,
    val status: String,
    val reuseCount: Int,
    val rtlsAttempts: Int,
    val rtlsLandings: Int,
    val asdsAttempts: Int,
    val asdsLandings: Int,
    val lastUpdate: String?,
    val launches: String // Stored as JSON string
) 