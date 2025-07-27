package com.jdf.spacexexplorer.domain.repository

import com.jdf.spacexexplorer.domain.model.Launch
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining the contract for SpaceX data operations.
 * This is part of the domain layer and defines what data the application needs.
 */
interface SpaceXRepository {
    
    /**
     * Get all launches as a Flow for reactive updates
     */
    fun getAllLaunches(): Flow<List<Launch>>
    
    /**
     * Get upcoming launches as a Flow
     */
    fun getUpcomingLaunches(): Flow<List<Launch>>
    
    /**
     * Get past launches with optional limit
     */
    fun getPastLaunches(limit: Int = 20): Flow<List<Launch>>
    
    /**
     * Get a specific launch by ID
     */
    suspend fun getLaunchById(launchId: String): Launch?
    
    /**
     * Get successful launches with optional limit
     */
    fun getSuccessfulLaunches(limit: Int = 10): Flow<List<Launch>>
    
    /**
     * Refresh launches from the remote API
     */
    suspend fun refreshLaunches()
    
    /**
     * Refresh upcoming launches from the remote API
     */
    suspend fun refreshUpcomingLaunches()
} 