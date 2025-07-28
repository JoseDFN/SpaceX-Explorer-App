package com.jdf.spacexexplorer.data.mappers

import com.jdf.spacexexplorer.data.local.entity.CapsuleEntity
import com.jdf.spacexexplorer.data.remote.dto.CapsuleDto
import com.jdf.spacexexplorer.domain.model.Capsule
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

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
        launches = launches.toJsonString(),
        dragonType = dragonType
    )
}

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