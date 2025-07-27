package com.jdf.spacexexplorer.domain.model

/**
 * Domain model representing a SpaceX launch with clean, essential properties
 * needed by the UI. This is the single source of truth for the presentation layer.
 */
data class Launch(
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
    val wikipediaUrl: String?
) 