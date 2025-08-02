package com.jdf.spacexexplorer.domain.model

/**
 * Domain model for a SpaceX crew member
 */
data class CrewMember(
    val id: String,
    val name: String,
    val agency: String,
    val image: String?,
    val wikipedia: String?,
    val launches: List<String>,
    val status: String
) 