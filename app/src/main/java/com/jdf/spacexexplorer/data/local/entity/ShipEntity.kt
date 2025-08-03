package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ships")
data class ShipEntity(
    @PrimaryKey val id: String,
    val name: String,
    val active: Boolean,
    val launches: String, // Stored as JSON string
    val homePort: String,
    val yearBuilt: Int?,
    val massKg: Double?,
    val massLbs: Double?,
    val type: String?,
    val roles: String, // Stored as JSON string
    val image: String?,
    val url: String?
) 