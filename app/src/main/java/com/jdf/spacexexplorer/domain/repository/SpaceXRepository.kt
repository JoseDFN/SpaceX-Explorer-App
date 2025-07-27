package com.jdf.spacexexplorer.domain.repository

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket
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
} 