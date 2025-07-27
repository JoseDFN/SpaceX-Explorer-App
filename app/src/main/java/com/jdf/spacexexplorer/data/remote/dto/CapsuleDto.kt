package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CapsuleDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "serial") val serial: String,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "reuse_count") val reuseCount: Int,
    @field:Json(name = "water_landings") val waterLandings: Int,
    @field:Json(name = "land_landings") val landLandings: Int,
    @field:Json(name = "last_update") val lastUpdate: String?,
    @field:Json(name = "launches") val launches: List<String>,
    @field:Json(name = "dragon_type") val dragonType: String?
) 