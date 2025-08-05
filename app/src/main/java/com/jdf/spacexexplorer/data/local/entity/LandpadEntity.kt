package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a landpad in the local database
 */
@Entity(tableName = "landpads")
data class LandpadEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val fullName: String,
    val type: String,
    val locality: String,
    val region: String,
    val latitude: Double?,
    val longitude: Double?,
    val landingAttempts: Int,
    val landingSuccesses: Int,
    val wikipediaUrl: String?,
    val details: String?,
    val status: String,
    val launches: String // Stored as JSON string
) 