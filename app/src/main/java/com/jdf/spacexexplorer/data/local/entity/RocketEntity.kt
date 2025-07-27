package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a rocket in the local database
 */
@Entity(tableName = "rockets")
data class RocketEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val description: String?,
    val height: Double?,
    val mass: Double?,
    val active: Boolean,
    val stages: Int,
    val boosters: Int,
    val costPerLaunch: Long?,
    val successRatePct: Int?,
    val firstFlight: String?,
    val country: String?,
    val company: String?,
    val wikipediaUrl: String?,
    val flickrImages: String // Stored as JSON string
) 