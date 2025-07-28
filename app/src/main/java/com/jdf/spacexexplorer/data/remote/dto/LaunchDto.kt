package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchDto(
    @field:Json(name = "id")
    val id: String,
    
    @field:Json(name = "name")
    val name: String,
    
    @field:Json(name = "date_utc")
    val dateUtc: String,
    
    @field:Json(name = "date_unix")
    val dateUnix: Long,
    
    @field:Json(name = "date_local")
    val dateLocal: String,
    
    @field:Json(name = "date_precision")
    val datePrecision: String,
    
    @field:Json(name = "static_fire_date_utc")
    val staticFireDateUtc: String?,
    
    @field:Json(name = "static_fire_date_unix")
    val staticFireDateUnix: Long?,
    
    @field:Json(name = "tbd")
    val tbd: Boolean,
    
    @field:Json(name = "net")
    val net: Boolean,
    
    @field:Json(name = "window")
    val window: Int?,
    
    @field:Json(name = "rocket")
    val rocket: String,
    
    @field:Json(name = "success")
    val success: Boolean?,
    
    @field:Json(name = "failures")
    val failures: List<FailureDto>?,
    
    @field:Json(name = "upcoming")
    val upcoming: Boolean,
    
    @field:Json(name = "details")
    val details: String?,
    
    @field:Json(name = "fairings")
    val fairings: FairingsDto?,
    
    @field:Json(name = "crew")
    val crew: List<String>?,
    
    @field:Json(name = "ships")
    val ships: List<String>?,
    
    @field:Json(name = "capsules")
    val capsules: List<String>?,
    
    @field:Json(name = "payloads")
    val payloads: List<String>?,
    
    @field:Json(name = "launchpad")
    val launchpad: String?,
    
    @field:Json(name = "flight_number")
    val flightNumber: Int,
    
    @field:Json(name = "links")
    val links: LinksDto?
)

@JsonClass(generateAdapter = true)
data class FailureDto(
    @field:Json(name = "time")
    val time: Int?,
    
    @field:Json(name = "altitude")
    val altitude: Int?,
    
    @field:Json(name = "reason")
    val reason: String
)

@JsonClass(generateAdapter = true)
data class FairingsDto(
    @field:Json(name = "reused")
    val reused: Boolean?,
    
    @field:Json(name = "recovery_attempt")
    val recoveryAttempt: Boolean?,
    
    @field:Json(name = "recovered")
    val recovered: Boolean?,
    
    @field:Json(name = "ships")
    val ships: List<String>?
)

@JsonClass(generateAdapter = true)
data class LinksDto(
    @field:Json(name = "patch")
    val patch: PatchDto?,
    
    @field:Json(name = "reddit")
    val reddit: RedditDto?,
    
    @field:Json(name = "flickr")
    val flickr: FlickrDto?,
    
    @field:Json(name = "presskit")
    val presskit: String?,
    
    @field:Json(name = "webcast")
    val webcast: String?,
    
    @field:Json(name = "youtube_id")
    val youtubeId: String?,
    
    @field:Json(name = "article")
    val article: String?,
    
    @field:Json(name = "wikipedia")
    val wikipedia: String?
)

@JsonClass(generateAdapter = true)
data class PatchDto(
    @field:Json(name = "small")
    val small: String?,
    
    @field:Json(name = "large")
    val large: String?
)

@JsonClass(generateAdapter = true)
data class RedditDto(
    @field:Json(name = "campaign")
    val campaign: String?,
    
    @field:Json(name = "launch")
    val launch: String?,
    
    @field:Json(name = "media")
    val media: String?,
    
    @field:Json(name = "recovery")
    val recovery: String?
)

@JsonClass(generateAdapter = true)
data class FlickrDto(
    @field:Json(name = "small")
    val small: List<String>?,
    
    @field:Json(name = "original")
    val original: List<String>?
) 