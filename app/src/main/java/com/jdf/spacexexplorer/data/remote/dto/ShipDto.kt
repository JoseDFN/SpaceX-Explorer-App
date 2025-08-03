package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShipDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "active") val active: Boolean,
    @field:Json(name = "launches") val launches: List<String>,
    @field:Json(name = "home_port") val homePort: String,
    @field:Json(name = "year_built") val yearBuilt: Int?,
    @field:Json(name = "mass_kg") val massKg: Double?,
    @field:Json(name = "mass_lbs") val massLbs: Double?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "roles") val roles: List<String>,
    @field:Json(name = "image") val image: String?,
    @field:Json(name = "url") val url: String?
) 