package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crew")
data class CrewEntity(
    @PrimaryKey val id: String,
    val name: String,
    val agency: String,
    val image: String?,
    val wikipedia: String?,
    val launches: String, // Stored as JSON string
    val status: String
) 