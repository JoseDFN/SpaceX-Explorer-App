package com.jdf.spacexexplorer.data.mappers

import com.jdf.spacexexplorer.data.local.entity.LaunchEntity
import com.jdf.spacexexplorer.data.local.entity.RocketEntity
import com.jdf.spacexexplorer.data.local.entity.CapsuleEntity
import com.jdf.spacexexplorer.data.remote.dto.LaunchDto
import com.jdf.spacexexplorer.data.remote.dto.RocketDto
import com.jdf.spacexexplorer.data.remote.dto.CapsuleDto
import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.Capsule
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Comprehensive data conversion utilities for the SpaceX Explorer app.
 * This file contains all extension functions to convert between different data models.
 */

// ============================================================================
// LAUNCH MAPPERS
// ============================================================================

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
        crew = crew?.toNullableStringJson(),
        ships = ships?.toNullableStringJson(),
        capsules = capsules?.toNullableStringJson(),
        payloads = payloads?.toNullableStringJson(),
        failures = failures?.toFailureDtoJson()
    )
}

/**
 * Convert list of LaunchDto to list of LaunchEntity
 */
fun List<LaunchDto>.toLaunchEntities(): List<LaunchEntity> {
    return this.map { it.toEntity() }
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
 * Convert list of LaunchEntity to list of Launch domain models
 */
fun List<LaunchEntity>.toLaunchDomainsFromEntity(): List<Launch> {
    return this.map { it.toDomain() }
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

/**
 * Convert list of LaunchDto to list of Launch domain models
 */
fun List<LaunchDto>.toLaunchDomainsFromDto(): List<Launch> {
    return this.map { it.toDomain() }
}

// ============================================================================
// ROCKET MAPPERS
// ============================================================================

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
        flickrImages = flickrImages.toStringJson()
    )
}

/**
 * Convert list of RocketDto to list of RocketEntity
 */
fun List<RocketDto>.toRocketEntities(): List<RocketEntity> {
    return this.map { it.toEntity() }
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
 * Convert list of RocketEntity to list of Rocket domain models
 */
fun List<RocketEntity>.toRocketDomainsFromEntity(): List<Rocket> {
    return this.map { it.toDomain() }
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

/**
 * Convert list of RocketDto to list of Rocket domain models
 */
fun List<RocketDto>.toRocketDomainsFromDto(): List<Rocket> {
    return this.map { it.toDomain() }
}

// ============================================================================
// CAPSULE MAPPERS
// ============================================================================

/**
 * Convert CapsuleDto to CapsuleEntity for database storage
 */
fun CapsuleDto.toEntity(): CapsuleEntity {
    return CapsuleEntity(
        id = id,
        serial = serial,
        type = type,
        status = status,
        reuseCount = reuseCount,
        waterLandings = waterLandings,
        landLandings = landLandings,
        lastUpdate = lastUpdate,
        launches = launches.toStringJson(),
        dragonType = dragonType
    )
}

/**
 * Convert list of CapsuleDto to list of CapsuleEntity
 */
fun List<CapsuleDto>.toCapsuleEntities(): List<CapsuleEntity> {
    return this.map { it.toEntity() }
}

/**
 * Convert CapsuleEntity to Capsule domain model
 */
fun CapsuleEntity.toDomain(): Capsule {
    return Capsule(
        id = id,
        serial = serial,
        type = type,
        status = status,
        reuseCount = reuseCount,
        waterLandings = waterLandings,
        landLandings = landLandings,
        lastUpdate = lastUpdate,
        launches = launches.fromJsonString(),
        dragonType = dragonType
    )
}

/**
 * Convert list of CapsuleEntity to list of Capsule domain models
 */
fun List<CapsuleEntity>.toCapsuleDomainsFromEntity(): List<Capsule> {
    return this.map { it.toDomain() }
}

/**
 * Convert CapsuleDto to Capsule domain model
 */
fun CapsuleDto.toDomain(): Capsule {
    return Capsule(
        id = id,
        serial = serial,
        type = type,
        status = status,
        reuseCount = reuseCount,
        waterLandings = waterLandings,
        landLandings = landLandings,
        lastUpdate = lastUpdate,
        launches = launches,
        dragonType = dragonType
    )
}

/**
 * Convert list of CapsuleDto to list of Capsule domain models
 */
fun List<CapsuleDto>.toCapsuleDomainsFromDto(): List<Capsule> {
    return this.map { it.toDomain() }
}

// ============================================================================
// HELPER FUNCTIONS FOR JSON CONVERSION
// ============================================================================

/**
 * Convert List<String> to JSON string for database storage
 */
private fun List<String>?.toNullableStringJson(): String? {
    return this?.let { list ->
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter = moshi.adapter<List<String>>(type)
        adapter.toJson(list)
    }
}

/**
 * Convert List<String> to JSON string for database storage (non-null version)
 */
private fun List<String>.toStringJson(): String {
    val moshi = Moshi.Builder().build()
    val type = Types.newParameterizedType(List::class.java, String::class.java)
    val adapter = moshi.adapter<List<String>>(type)
    return adapter.toJson(this)
}

/**
 * Convert JSON string back to List<String> for domain model
 */
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

/**
 * Convert List<FailureDto> to JSON string for database storage
 */
private fun List<com.jdf.spacexexplorer.data.remote.dto.FailureDto>?.toFailureDtoJson(): String? {
    return this?.let { list ->
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, com.jdf.spacexexplorer.data.remote.dto.FailureDto::class.java)
        val adapter = moshi.adapter<List<com.jdf.spacexexplorer.data.remote.dto.FailureDto>>(type)
        adapter.toJson(list)
    }
} 