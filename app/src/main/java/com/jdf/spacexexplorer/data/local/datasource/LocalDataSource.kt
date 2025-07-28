package com.jdf.spacexexplorer.data.local.datasource

import com.jdf.spacexexplorer.data.local.LaunchDao
import com.jdf.spacexexplorer.data.local.RocketDao
import com.jdf.spacexexplorer.data.local.entity.LaunchEntity
import com.jdf.spacexexplorer.data.local.entity.RocketEntity
import com.jdf.spacexexplorer.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Local data source for handling database operations
 */
class LocalDataSource @Inject constructor(
    private val launchDao: LaunchDao,
    private val rocketDao: RocketDao
) {
    
    /**
     * Get all launches as a Flow
     */
    fun getAllLaunches(): Flow<Result<List<LaunchEntity>>> {
        return launchDao.getAllLaunches().map { launches ->
            Result.success(launches)
        }
    }
    
    /**
     * Get upcoming launches as a Flow
     */
    fun getUpcomingLaunches(): Flow<Result<List<LaunchEntity>>> {
        return launchDao.getUpcomingLaunches().map { launches ->
            Result.success(launches)
        }
    }
    
    /**
     * Get past launches as a Flow
     */
    fun getPastLaunches(limit: Int = 20): Flow<Result<List<LaunchEntity>>> {
        return launchDao.getPastLaunches(limit).map { launches ->
            Result.success(launches)
        }
    }
    
    /**
     * Get a specific launch by ID
     */
    suspend fun getLaunchById(launchId: String): Result<LaunchEntity?> {
        return try {
            val launch = launchDao.getLaunchById(launchId)
            Result.success(launch)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Get successful launches as a Flow
     */
    fun getSuccessfulLaunches(limit: Int = 10): Flow<Result<List<LaunchEntity>>> {
        return launchDao.getSuccessfulLaunches(limit).map { launches ->
            Result.success(launches)
        }
    }
    
    /**
     * Insert a single launch
     */
    suspend fun insertLaunch(launch: LaunchEntity): Result<Unit> {
        return try {
            launchDao.insertLaunch(launch)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Insert multiple launches
     */
    suspend fun insertLaunches(launches: List<LaunchEntity>): Result<Unit> {
        return try {
            launchDao.insertLaunches(launches)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Delete all launches and insert new ones (transaction)
     */
    suspend fun deleteAllAndInsertLaunches(launches: List<LaunchEntity>): Result<Unit> {
        return try {
            launchDao.deleteAllAndInsertLaunches(launches)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Delete upcoming launches and insert new ones (transaction)
     */
    suspend fun deleteAllAndInsertUpcomingLaunches(launches: List<LaunchEntity>): Result<Unit> {
        return try {
            launchDao.deleteAllAndInsertUpcomingLaunches(launches)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Delete all launches
     */
    suspend fun deleteAllLaunches(): Result<Unit> {
        return try {
            launchDao.deleteAllLaunches()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    // Rocket operations
    
    /**
     * Get all rockets as a Flow
     */
    fun getAllRockets(): Flow<Result<List<RocketEntity>>> {
        return rocketDao.getRockets().map { rockets ->
            Result.success(rockets)
        }
    }
    
    /**
     * Get a specific rocket by ID
     */
    suspend fun getRocketById(rocketId: String): Result<RocketEntity?> {
        return try {
            val rocket = rocketDao.getRocketById(rocketId)
            Result.success(rocket)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Insert multiple rockets
     */
    suspend fun insertRockets(rockets: List<RocketEntity>): Result<Unit> {
        return try {
            rocketDao.insertRockets(rockets)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Delete all rockets and insert new ones (transaction)
     */
    suspend fun deleteAllAndInsertRockets(rockets: List<RocketEntity>): Result<Unit> {
        return try {
            rocketDao.deleteAllRockets()
            rocketDao.insertRockets(rockets)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Delete all rockets
     */
    suspend fun deleteAllRockets(): Result<Unit> {
        return try {
            rocketDao.deleteAllRockets()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
} 