package com.jdf.spacexexplorer.data.local.datasource

import com.jdf.spacexexplorer.data.local.LaunchDao
import com.jdf.spacexexplorer.data.local.entity.LaunchEntity
import com.jdf.spacexexplorer.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Local data source for handling database operations
 */
class LocalDataSource @Inject constructor(
    private val launchDao: LaunchDao
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
} 