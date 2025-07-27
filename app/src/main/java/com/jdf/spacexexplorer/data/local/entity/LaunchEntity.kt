package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey
    val id: String,
    val missionName: String,
    val launchDate: String,
    val launchDateUnix: Long,
    val wasSuccessful: Boolean?,
    val isUpcoming: Boolean,
    val patchImageUrl: String?,
    val details: String?,
    val rocketId: String,
    val flightNumber: Int,
    val webcastUrl: String?,
    val articleUrl: String?,
    val wikipediaUrl: String?,
    // Additional fields for complete data storage
    val dateUtc: String,
    val dateLocal: String,
    val datePrecision: String,
    val staticFireDateUtc: String?,
    val staticFireDateUnix: Long?,
    val tbd: Boolean,
    val net: Boolean,
    val window: Int?,
    val launchpad: String?,
    val crew: String?, // JSON string for List<String>
    val ships: String?, // JSON string for List<String>
    val capsules: String?, // JSON string for List<String>
    val payloads: String?, // JSON string for List<String>
    val failures: String? // JSON string for List<FailureDto>
) 