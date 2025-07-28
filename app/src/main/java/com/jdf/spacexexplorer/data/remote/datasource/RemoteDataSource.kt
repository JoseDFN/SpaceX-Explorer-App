package com.jdf.spacexexplorer.data.remote.datasource

import com.jdf.spacexexplorer.data.remote.ApiService
import com.jdf.spacexexplorer.data.remote.dto.LaunchDto
import com.jdf.spacexexplorer.data.remote.dto.RocketDto
import com.jdf.spacexexplorer.domain.model.Result
import javax.inject.Inject

/**
 * Remote data source for handling API calls to SpaceX API
 */
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    
    /**
     * Fetch all launches from the API
     */
    suspend fun getLaunches(limit: Int = 20, offset: Int = 0): Result<List<LaunchDto>> {
        return try {
            val launches = apiService.getLaunches(limit, offset)
            Result.success(launches)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Fetch upcoming launches from the API
     */
    suspend fun getUpcomingLaunches(limit: Int = 10): Result<List<LaunchDto>> {
        return try {
            val launches = apiService.getUpcomingLaunches(limit)
            Result.success(launches)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Fetch past launches from the API
     */
    suspend fun getPastLaunches(limit: Int = 20, offset: Int = 0): Result<List<LaunchDto>> {
        return try {
            val launches = apiService.getPastLaunches(limit, offset)
            Result.success(launches)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Fetch the latest launch from the API
     */
    suspend fun getLatestLaunch(): Result<LaunchDto> {
        return try {
            val launch = apiService.getLatestLaunch()
            Result.success(launch)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Fetch all rockets from the API
     */
    suspend fun getRockets(): Result<List<RocketDto>> {
        return try {
            val rockets = apiService.getRockets()
            Result.success(rockets)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Fetch a specific rocket by ID from the API
     */
    suspend fun getRocketById(id: String): Result<RocketDto> {
        return try {
            val rocket = apiService.getRocketById(id)
            Result.success(rocket)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
} 