package com.jdf.spacexexplorer.data.mappers

import com.jdf.spacexexplorer.data.local.entity.RocketEntity
import com.jdf.spacexexplorer.data.remote.dto.RocketDto
import com.jdf.spacexexplorer.domain.model.Rocket
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Extension functions to convert between different data models for Rocket
 */

/**
 * Convert RocketDto to RocketEntity for database storage
 */
fun RocketDto.toEntity(): RocketEntity {
    return RocketEntity(
        id = id,
        name = name,
        type = type,
        description = description,
        height = height?.meters,
        mass = mass?.kg,
        active = active,
        stages = stages,
        boosters = boosters,
        costPerLaunch = costPerLaunch,
        successRatePct = successRatePct,
        firstFlight = firstFlight,
        country = country,
        company = company,
        wikipediaUrl = wikipediaUrl,
        flickrImages = flickrImages.toJsonString()
    )
}

/**
 * Convert RocketEntity to Rocket domain model
 */
fun RocketEntity.toDomain(): Rocket {
    return Rocket(
        id = id,
        name = name,
        type = type,
        description = description,
        height = height,
        mass = mass,
        active = active,
        stages = stages,
        boosters = boosters,
        costPerLaunch = costPerLaunch,
        successRatePct = successRatePct,
        firstFlight = firstFlight,
        country = country,
        company = company,
        wikipediaUrl = wikipediaUrl,
        flickrImages = flickrImages.fromJsonString()
    )
}

/**
 * Convert RocketDto to Rocket domain model
 */
fun RocketDto.toDomain(): Rocket {
    return Rocket(
        id = id,
        name = name,
        type = type,
        description = description,
        height = height?.meters,
        mass = mass?.kg,
        active = active,
        stages = stages,
        boosters = boosters,
        costPerLaunch = costPerLaunch,
        successRatePct = successRatePct,
        firstFlight = firstFlight,
        country = country,
        company = company,
        wikipediaUrl = wikipediaUrl,
        flickrImages = flickrImages
    )
}

// Helper extension functions for JSON conversion
private fun List<String>.toJsonString(): String {
    val moshi = Moshi.Builder().build()
    val type = Types.newParameterizedType(List::class.java, String::class.java)
    val adapter = moshi.adapter<List<String>>(type)
    return adapter.toJson(this)
}

private fun String.fromJsonString(): List<String> {
    return try {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter = moshi.adapter<List<String>>(type)
        adapter.fromJson(this) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
} 