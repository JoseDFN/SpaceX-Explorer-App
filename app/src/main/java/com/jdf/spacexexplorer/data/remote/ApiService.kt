package com.jdf.spacexexplorer.data.remote

import com.jdf.spacexexplorer.data.remote.dto.LaunchDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for SpaceX API endpoints
 */
interface ApiService {
    
    /**
     * Get all launches with pagination support
     */
    @GET("launches")
    suspend fun getLaunches(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<LaunchDto>
    
    /**
     * Get the latest launch
     */
    @GET("launches/latest")
    suspend fun getLatestLaunch(): LaunchDto
    
    /**
     * Get upcoming launches
     */
    @GET("launches/upcoming")
    suspend fun getUpcomingLaunches(
        @Query("limit") limit: Int = 10
    ): List<LaunchDto>
    
    /**
     * Get past launches
     */
    @GET("launches/past")
    suspend fun getPastLaunches(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<LaunchDto>

    // New endpoint for fetching a single launch by ID
    @GET("launches/{id}")
    suspend fun getLaunchById(@Path("id") id: String): LaunchDto
} 