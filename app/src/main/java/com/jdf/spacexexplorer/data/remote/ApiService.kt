package com.jdf.spacexexplorer.data.remote

import com.jdf.spacexexplorer.data.remote.dto.LaunchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    
    @GET("launches")
    suspend fun getLaunches(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<LaunchDto>
    
    @GET("launches/latest")
    suspend fun getLatestLaunch(): LaunchDto
    
    @GET("launches/upcoming")
    suspend fun getUpcomingLaunches(
        @Query("limit") limit: Int = 10
    ): List<LaunchDto>
} 