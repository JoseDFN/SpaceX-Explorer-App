package com.jdf.spacexexplorer.data.repository

import com.jdf.spacexexplorer.data.local.LaunchDao
import com.jdf.spacexexplorer.data.mappers.toLaunch
import com.jdf.spacexexplorer.data.mappers.toLaunchEntity
import com.jdf.spacexexplorer.data.remote.ApiService
import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Implementation of SpaceXRepository that follows the single source of truth strategy.
 * The local database (Room) is the single source of truth for the UI.
 * 
 * Data Flow Strategy:
 * 1. First, emit data from local database (immediate response)
 * 2. Then, fetch fresh data from remote API
 * 3. On success, update local database with new data
 * 4. On failure, keep local data and emit error
 */
class SpaceXRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val launchDao: LaunchDao
) : SpaceXRepository {
    
    override fun getLaunches(): Flow<Result<List<Launch>>> {
        return launchDao.getAllLaunches()
            .onStart {
                // Trigger network refresh when flow starts
                refreshLaunchesFromNetwork()
            }
            .map { entities ->
                val launches = entities.map { it.toLaunch() }
                Result.success(launches)
            }
            .catch { e ->
                emit(Result.error(e))
            }
    }
    
    override fun getUpcomingLaunches(): Flow<Result<List<Launch>>> {
        return launchDao.getUpcomingLaunches()
            .onStart {
                // Trigger network refresh when flow starts
                refreshUpcomingLaunchesFromNetwork()
            }
            .map { entities ->
                val launches = entities.map { it.toLaunch() }
                Result.success(launches)
            }
            .catch { e ->
                emit(Result.error(e))
            }
    }
    
    override fun getPastLaunches(limit: Int): Flow<Result<List<Launch>>> {
        return launchDao.getPastLaunches(limit)
            .map { entities ->
                val launches = entities.map { it.toLaunch() }
                Result.success(launches)
            }
            .catch { e ->
                emit(Result.error(e))
            }
    }
    
    override suspend fun getLaunchById(launchId: String): Result<Launch?> {
        return try {
            val entity = launchDao.getLaunchById(launchId)
            val launch = entity?.toLaunch()
            Result.success(launch)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override fun getSuccessfulLaunches(limit: Int): Flow<Result<List<Launch>>> {
        return launchDao.getSuccessfulLaunches(limit)
            .map { entities ->
                val launches = entities.map { it.toLaunch() }
                Result.success(launches)
            }
            .catch { e ->
                emit(Result.error(e))
            }
    }
    
    override suspend fun refreshLaunches(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteLaunches = apiService.getLaunches()
            
            // Convert DTOs to entities
            val entities = remoteLaunches.map { it.toLaunchEntity() }
            
            // Use transactional "delete all and insert new" strategy
            launchDao.deleteAllAndInsertLaunches(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshUpcomingLaunches(): Result<Unit> {
        return try {
            // Fetch fresh upcoming launches from API
            val remoteLaunches = apiService.getUpcomingLaunches()
            
            // Convert DTOs to entities
            val entities = remoteLaunches.map { it.toLaunchEntity() }
            
            // Use transactional "delete all and insert new" strategy for upcoming launches
            launchDao.deleteAllAndInsertUpcomingLaunches(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh launches from network without blocking the flow
     */
    private suspend fun refreshLaunchesFromNetwork() {
        try {
            val remoteLaunches = apiService.getLaunches()
            val entities = remoteLaunches.map { it.toLaunchEntity() }
            launchDao.deleteAllAndInsertLaunches(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
    
    /**
     * Private method to refresh upcoming launches from network without blocking the flow
     */
    private suspend fun refreshUpcomingLaunchesFromNetwork() {
        try {
            val remoteLaunches = apiService.getUpcomingLaunches()
            val entities = remoteLaunches.map { it.toLaunchEntity() }
            launchDao.deleteAllAndInsertUpcomingLaunches(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
} 