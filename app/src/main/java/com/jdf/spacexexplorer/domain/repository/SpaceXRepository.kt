package com.jdf.spacexexplorer.domain.repository

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.Capsule
import com.jdf.spacexexplorer.domain.model.Core
import com.jdf.spacexexplorer.domain.model.CrewMember
import com.jdf.spacexexplorer.domain.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining the contract for SpaceX data operations.
 * This is part of the domain layer and defines what data the application needs.
 */
interface SpaceXRepository {
    
    /**
     * Get all launches as a Flow for reactive updates with Result wrapper
     */
    fun getLaunches(): Flow<Result<List<Launch>>>
    
    /**
     * Get upcoming launches as a Flow with Result wrapper
     */
    fun getUpcomingLaunches(): Flow<Result<List<Launch>>>
    
    /**
     * Get past launches with optional limit and Result wrapper
     */
    fun getPastLaunches(limit: Int = 20): Flow<Result<List<Launch>>>
    
    /**
     * Get a specific launch by ID with Result wrapper
     */
    suspend fun getLaunchById(id: String): Result<Launch>
    
    /**
     * Get successful launches with optional limit and Result wrapper
     */
    fun getSuccessfulLaunches(limit: Int = 10): Flow<Result<List<Launch>>>
    
    /**
     * Refresh launches from the remote API
     */
    suspend fun refreshLaunches(): Result<Unit>
    
    /**
     * Refresh upcoming launches from the remote API
     */
    suspend fun refreshUpcomingLaunches(): Result<Unit>
    
    /**
     * Get all rockets as a Flow for reactive updates with Result wrapper
     */
    fun getRockets(): Flow<Result<List<Rocket>>>
    
    /**
     * Get a specific rocket by ID with Result wrapper
     */
    suspend fun getRocketById(id: String): Result<Rocket>
    
    /**
     * Refresh rockets from the remote API
     */
    suspend fun refreshRockets(): Result<Unit>
    
    /**
     * Get all capsules as a Flow for reactive updates with Result wrapper
     */
    fun getCapsules(): Flow<Result<List<Capsule>>>
    
    /**
     * Get a specific capsule by ID with Result wrapper
     */
    suspend fun getCapsuleById(id: String): Result<Capsule>
    
    /**
     * Refresh capsules from the remote API
     */
    suspend fun refreshCapsules(): Result<Unit>
    
    /**
     * Get all cores as a Flow for reactive updates with Result wrapper
     */
    fun getCores(): Flow<Result<List<Core>>>
    
    /**
     * Get a specific core by ID with Result wrapper
     */
    suspend fun getCoreById(id: String): Result<Core>
    
    /**
     * Refresh cores from the remote API
     */
    suspend fun refreshCores(): Result<Unit>
    
    /**
     * Get all crew members as a Flow for reactive updates with Result wrapper
     */
    fun getCrew(): Flow<Result<List<CrewMember>>>
    
    /**
     * Get a specific crew member by ID with Result wrapper
     */
    suspend fun getCrewById(id: String): Result<CrewMember>
    
    /**
     * Refresh crew members from the remote API
     */
    suspend fun refreshCrew(): Result<Unit>
} 