package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO representing a landpad from the SpaceX API
 */
@JsonClass(generateAdapter = true)
data class LandpadDto(
    @field:Json(name = "id")
    val id: String,
    
    @field:Json(name = "name")
    val name: String,
    
    @field:Json(name = "full_name")
    val fullName: String,
    
    @field:Json(name = "type")
    val type: String,
    
    @field:Json(name = "locality")
    val locality: String,
    
    @field:Json(name = "region")
    val region: String,
    
    @field:Json(name = "latitude")
    val latitude: Double?,
    
    @field:Json(name = "longitude")
    val longitude: Double?,
    
    @field:Json(name = "landing_attempts")
    val landingAttempts: Int,
    
    @field:Json(name = "landing_successes")
    val landingSuccesses: Int,
    
    @field:Json(name = "wikipedia")
    val wikipediaUrl: String?,
    
    @field:Json(name = "details")
    val details: String?,
    
    @field:Json(name = "status")
    val status: String,
    
    @field:Json(name = "launches")
    val launches: List<String>
) 