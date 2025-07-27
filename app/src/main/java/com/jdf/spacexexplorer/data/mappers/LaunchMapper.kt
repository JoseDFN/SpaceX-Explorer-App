package com.jdf.spacexexplorer.data.mappers

import com.jdf.spacexexplorer.data.local.entity.LaunchEntity
import com.jdf.spacexexplorer.data.remote.dto.LaunchDto
import com.jdf.spacexexplorer.domain.model.Launch
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Extension functions to convert between different data models for Launch
 */

/**
 * Convert LaunchDto to LaunchEntity for database storage
 */
fun LaunchDto.toEntity(): LaunchEntity {
    return LaunchEntity(
        id = id,
        missionName = name,
        launchDate = dateUtc,
        launchDateUnix = dateUnix,
        wasSuccessful = success,
        isUpcoming = upcoming,
        patchImageUrl = links?.patch?.large ?: links?.patch?.small,
        details = details,
        rocketId = rocket,
        flightNumber = flightNumber,
        webcastUrl = links?.webcast,
        articleUrl = links?.article,
        wikipediaUrl = links?.wikipedia,
        // Additional fields for complete data storage
        dateUtc = dateUtc,
        dateLocal = dateLocal,
        datePrecision = datePrecision,
        staticFireDateUtc = staticFireDateUtc,
        staticFireDateUnix = staticFireDateUnix,
        tbd = tbd,
        net = net,
        window = window,
        launchpad = launchpad,
        crew = crew?.toJsonString(),
        ships = ships?.toJsonString(),
        capsules = capsules?.toJsonString(),
        payloads = payloads?.toJsonString(),
        failures = failures?.toJsonString()
    )
}

/**
 * Convert LaunchEntity to Launch domain model
 */
fun LaunchEntity.toDomain(): Launch {
    return Launch(
        id = id,
        missionName = missionName,
        launchDate = launchDate,
        launchDateUnix = launchDateUnix,
        wasSuccessful = wasSuccessful,
        isUpcoming = isUpcoming,
        patchImageUrl = patchImageUrl,
        details = details,
        rocketId = rocketId,
        flightNumber = flightNumber,
        webcastUrl = webcastUrl,
        articleUrl = articleUrl,
        wikipediaUrl = wikipediaUrl
    )
}

/**
 * Convert LaunchDto to Launch domain model
 */
fun LaunchDto.toDomain(): Launch {
    return Launch(
        id = id,
        missionName = name,
        launchDate = dateUtc,
        launchDateUnix = dateUnix,
        wasSuccessful = success,
        isUpcoming = upcoming,
        patchImageUrl = links?.patch?.large ?: links?.patch?.small,
        details = details,
        rocketId = rocket,
        flightNumber = flightNumber,
        webcastUrl = links?.webcast,
        articleUrl = links?.article,
        wikipediaUrl = links?.wikipedia
    )
}

// Helper extension functions for JSON conversion
private fun List<String>?.toJsonString(): String? {
    return this?.let { list ->
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter = moshi.adapter<List<String>>(type)
        adapter.toJson(list)
    }
}

private fun List<com.jdf.spacexexplorer.data.remote.dto.FailureDto>?.toJsonString(): String? {
    return this?.let { list ->
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, com.jdf.spacexexplorer.data.remote.dto.FailureDto::class.java)
        val adapter = moshi.adapter<List<com.jdf.spacexexplorer.data.remote.dto.FailureDto>>(type)
        adapter.toJson(list)
    }
} 