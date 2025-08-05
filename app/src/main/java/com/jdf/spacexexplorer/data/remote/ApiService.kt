package com.jdf.spacexexplorer.data.remote

import com.jdf.spacexexplorer.data.remote.dto.LaunchDto
import com.jdf.spacexexplorer.data.remote.dto.RocketDto
import com.jdf.spacexexplorer.data.remote.dto.CapsuleDto
import com.jdf.spacexexplorer.data.remote.dto.CoreDto
import com.jdf.spacexexplorer.data.remote.dto.CrewDto
import com.jdf.spacexexplorer.data.remote.dto.ShipDto
import com.jdf.spacexexplorer.data.remote.dto.DragonDto
import com.jdf.spacexexplorer.data.remote.dto.LandpadDto
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
    
    /**
     * Get all rockets
     */
    @GET("rockets")
    suspend fun getRockets(): List<RocketDto>
    
    /**
     * Get a specific rocket by ID
     */
    @GET("rockets/{id}")
    suspend fun getRocketById(@Path("id") id: String): RocketDto
    
    /**
     * Get all capsules
     */
    @GET("capsules")
    suspend fun getCapsules(): List<CapsuleDto>
    
    /**
     * Get a specific capsule by ID
     */
    @GET("capsules/{id}")
    suspend fun getCapsuleById(@Path("id") id: String): CapsuleDto
    
    /**
     * Get all cores
     */
    @GET("cores")
    suspend fun getCores(): List<CoreDto>
    
    /**
     * Get a specific core by ID
     */
    @GET("cores/{id}")
    suspend fun getCoreById(@Path("id") id: String): CoreDto
    
    /**
     * Get all crew members
     */
    @GET("crew")
    suspend fun getCrew(): List<CrewDto>
    
    /**
     * Get a specific crew member by ID
     */
    @GET("crew/{id}")
    suspend fun getCrewById(@Path("id") id: String): CrewDto
    
    /**
     * Get all ships
     */
    @GET("ships")
    suspend fun getShips(): List<ShipDto>
    
    /**
     * Get a specific ship by ID
     */
    @GET("ships/{id}")
    suspend fun getShipById(@Path("id") id: String): ShipDto
    
    /**
     * Get all dragons
     */
    @GET("dragons")
    suspend fun getDragons(): List<DragonDto>
    
    /**
     * Get a specific dragon by ID
     */
    @GET("dragons/{id}")
    suspend fun getDragonById(@Path("id") id: String): DragonDto
    
    /**
     * Get all landpads
     */
    @GET("landpads")
    suspend fun getLandpads(): List<LandpadDto>
    
    /**
     * Get a specific landpad by ID
     */
    @GET("landpads/{id}")
    suspend fun getLandpadById(@Path("id") id: String): LandpadDto
} 