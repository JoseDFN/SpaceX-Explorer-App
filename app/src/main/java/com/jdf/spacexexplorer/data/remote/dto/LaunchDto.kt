package com.jdf.spacexexplorer.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchDto(
    @Json(name = "id")
    val id: String,
    
    @Json(name = "name")
    val name: String,
    
    @Json(name = "date_utc")
    val dateUtc: String,
    
    @Json(name = "date_unix")
    val dateUnix: Long,
    
    @Json(name = "date_local")
    val dateLocal: String,
    
    @Json(name = "date_precision")
    val datePrecision: String,
    
    @Json(name = "static_fire_date_utc")
    val staticFireDateUtc: String?,
    
    @Json(name = "static_fire_date_unix")
    val staticFireDateUnix: Long?,
    
    @Json(name = "tbd")
    val tbd: Boolean,
    
    @Json(name = "net")
    val net: Boolean,
    
    @Json(name = "window")
    val window: Int?,
    
    @Json(name = "rocket")
    val rocket: String,
    
    @Json(name = "success")
    val success: Boolean?,
    
    @Json(name = "failures")
    val failures: List<FailureDto>?,
    
    @Json(name = "upcoming")
    val upcoming: Boolean,
    
    @Json(name = "details")
    val details: String?,
    
    @Json(name = "fairings")
    val fairings: FairingsDto?,
    
    @Json(name = "crew")
    val crew: List<String>?,
    
    @Json(name = "ships")
    val ships: List<String>?,
    
    @Json(name = "capsules")
    val capsules: List<String>?,
    
    @Json(name = "payloads")
    val payloads: List<String>?,
    
    @Json(name = "launchpad")
    val launchpad: String?,
    
    @Json(name = "flight_number")
    val flightNumber: Int,
    
    @Json(name = "name")
    val missionName: String,
    
    @Json(name = "date_utc")
    val launchDateUtc: String,
    
    @Json(name = "date_unix")
    val launchDateUnix: Long,
    
    @Json(name = "date_local")
    val launchDateLocal: String,
    
    @Json(name = "date_precision")
    val launchDatePrecision: String,
    
    @Json(name = "upcoming")
    val isUpcoming: Boolean,
    
    @Json(name = "success")
    val isSuccess: Boolean?,
    
    @Json(name = "details")
    val missionDetails: String?,
    
    @Json(name = "links")
    val links: LinksDto?
)

@JsonClass(generateAdapter = true)
data class FailureDto(
    @Json(name = "time")
    val time: Int?,
    
    @Json(name = "altitude")
    val altitude: Int?,
    
    @Json(name = "reason")
    val reason: String
)

@JsonClass(generateAdapter = true)
data class FairingsDto(
    @Json(name = "reused")
    val reused: Boolean?,
    
    @Json(name = "recovery_attempt")
    val recoveryAttempt: Boolean?,
    
    @Json(name = "recovered")
    val recovered: Boolean?,
    
    @Json(name = "ships")
    val ships: List<String>?
)

@JsonClass(generateAdapter = true)
data class LinksDto(
    @Json(name = "patch")
    val patch: PatchDto?,
    
    @Json(name = "reddit")
    val reddit: RedditDto?,
    
    @Json(name = "flickr")
    val flickr: FlickrDto?,
    
    @Json(name = "presskit")
    val presskit: String?,
    
    @Json(name = "webcast")
    val webcast: String?,
    
    @Json(name = "youtube_id")
    val youtubeId: String?,
    
    @Json(name = "article")
    val article: String?,
    
    @Json(name = "wikipedia")
    val wikipedia: String?
)

@JsonClass(generateAdapter = true)
data class PatchDto(
    @Json(name = "small")
    val small: String?,
    
    @Json(name = "large")
    val large: String?
)

@JsonClass(generateAdapter = true)
data class RedditDto(
    @Json(name = "campaign")
    val campaign: String?,
    
    @Json(name = "launch")
    val launch: String?,
    
    @Json(name = "media")
    val media: String?,
    
    @Json(name = "recovery")
    val recovery: String?
)

@JsonClass(generateAdapter = true)
data class FlickrDto(
    @Json(name = "small")
    val small: List<String>?,
    
    @Json(name = "original")
    val original: List<String>?
) 