package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO representing a rocket from the SpaceX API
 */
@JsonClass(generateAdapter = true)
data class RocketDto(
    @field:Json(name = "id")
    val id: String,
    
    @field:Json(name = "name")
    val name: String,
    
    @field:Json(name = "type")
    val type: String,
    
    @field:Json(name = "description")
    val description: String?,
    
    @field:Json(name = "height")
    val height: HeightDto?,
    
    @field:Json(name = "mass")
    val mass: MassDto?,
    
    @field:Json(name = "active")
    val active: Boolean,
    
    @field:Json(name = "stages")
    val stages: Int,
    
    @field:Json(name = "boosters")
    val boosters: Int,
    
    @field:Json(name = "cost_per_launch")
    val costPerLaunch: Long?,
    
    @field:Json(name = "success_rate_pct")
    val successRatePct: Int?,
    
    @field:Json(name = "first_flight")
    val firstFlight: String?,
    
    @field:Json(name = "country")
    val country: String?,
    
    @field:Json(name = "company")
    val company: String?,
    
    @field:Json(name = "wikipedia")
    val wikipediaUrl: String?,
    
    @field:Json(name = "flickr_images")
    val flickrImages: List<String>
)

/**
 * DTO for rocket height measurements
 */
@JsonClass(generateAdapter = true)
data class HeightDto(
    @field:Json(name = "meters")
    val meters: Double?,
    
    @field:Json(name = "feet")
    val feet: Double?
)

/**
 * DTO for rocket mass measurements
 */
@JsonClass(generateAdapter = true)
data class MassDto(
    @field:Json(name = "kg")
    val kg: Double?,
    
    @field:Json(name = "lb")
    val lb: Double?
) 