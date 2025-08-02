package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CrewDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "agency") val agency: String,
    @field:Json(name = "image") val image: String?,
    @field:Json(name = "wikipedia") val wikipedia: String?,
    @field:Json(name = "launches") val launches: List<String>,
    @field:Json(name = "status") val status: String
) 