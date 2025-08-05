package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO representing a launchpad from the SpaceX API
 */
@JsonClass(generateAdapter = true)
data class LaunchpadDto(
    @field:Json(name = "id")
    val id: String,
    
    @field:Json(name = "name")
    val name: String,
    
    @field:Json(name = "full_name")
    val fullName: String,
    
    @field:Json(name = "locality")
    val locality: String,
    
    @field:Json(name = "region")
    val region: String,
    
    @field:Json(name = "latitude")
    val latitude: Double?,
    
    @field:Json(name = "longitude")
    val longitude: Double?,
    
    @field:Json(name = "launch_attempts")
    val launchAttempts: Int,
    
    @field:Json(name = "launch_successes")
    val launchSuccesses: Int,
    
    @field:Json(name = "rockets")
    val rockets: List<String>,
    
    @field:Json(name = "timezone")
    val timezone: String,
    
    @field:Json(name = "launches")
    val launches: List<String>,
    
    @field:Json(name = "status")
    val status: String,
    
    @field:Json(name = "details")
    val details: String?,
    
    @field:Json(name = "images")
    val images: LaunchpadImagesDto?
)

@JsonClass(generateAdapter = true)
data class LaunchpadImagesDto(
    @field:Json(name = "large")
    val large: List<String>
) 