package com.jdf.spacexexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val dateUtc: String,
    val dateUnix: Long,
    val dateLocal: String,
    val datePrecision: String,
    val staticFireDateUtc: String?,
    val staticFireDateUnix: Long?,
    val tbd: Boolean,
    val net: Boolean,
    val window: Int?,
    val rocket: String,
    val success: Boolean?,
    val upcoming: Boolean,
    val details: String?,
    val crew: String?, // JSON string for List<String>
    val ships: String?, // JSON string for List<String>
    val capsules: String?, // JSON string for List<String>
    val payloads: String?, // JSON string for List<String>
    val launchpad: String?,
    val flightNumber: Int,
    val missionName: String,
    val launchDateUtc: String,
    val launchDateUnix: Long,
    val launchDateLocal: String,
    val launchDatePrecision: String,
    val isUpcoming: Boolean,
    val isSuccess: Boolean?,
    val missionDetails: String?,
    val patchSmall: String?,
    val patchLarge: String?,
    val webcast: String?,
    val youtubeId: String?,
    val article: String?,
    val wikipedia: String?
) 