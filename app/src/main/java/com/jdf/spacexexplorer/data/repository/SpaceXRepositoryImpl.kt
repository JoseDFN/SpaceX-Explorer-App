package com.jdf.spacexexplorer.data.repository

import com.jdf.spacexexplorer.data.local.LaunchDao
import com.jdf.spacexexplorer.data.local.RocketDao
import com.jdf.spacexexplorer.data.local.CapsuleDao
import com.jdf.spacexexplorer.data.mappers.toDomain
import com.jdf.spacexexplorer.data.mappers.toEntity
import com.jdf.spacexexplorer.data.remote.ApiService
import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.Capsule
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
    private val launchDao: LaunchDao,
    private val rocketDao: RocketDao,
    private val capsuleDao: CapsuleDao
) : SpaceXRepository {
    
    override fun getLaunches(): Flow<Result<List<Launch>>> {
        return launchDao.getAllLaunches()
            .onStart {
                // Trigger network refresh when flow starts
                refreshLaunchesFromNetwork()
            }
            .map { entities ->
                val launches = entities.map { it.toDomain() }
                Result.success(launches)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override fun getUpcomingLaunches(): Flow<Result<List<Launch>>> {
        return launchDao.getUpcomingLaunches()
            .onStart {
                // Trigger network refresh when flow starts
                refreshUpcomingLaunchesFromNetwork()
            }
            .map { entities ->
                val launches = entities.map { it.toDomain() }
                Result.success(launches)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override fun getPastLaunches(limit: Int): Flow<Result<List<Launch>>> {
        return launchDao.getPastLaunches(limit)
            .map { entities ->
                val launches = entities.map { it.toDomain() }
                Result.success(launches)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getLaunchById(id: String): Result<Launch> {
        return try {
            // Try to get from local database first
            val localEntity = launchDao.getLaunchById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getLaunchById(id)
            val entity = remoteDto.toEntity()
            launchDao.insertLaunch(entity)
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override fun getSuccessfulLaunches(limit: Int): Flow<Result<List<Launch>>> {
        return launchDao.getSuccessfulLaunches(limit)
            .map { entities ->
                val launches = entities.map { it.toDomain() }
                Result.success(launches)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun refreshLaunches(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteLaunches = apiService.getLaunches()
            
            // Convert DTOs to entities
            val entities = remoteLaunches.map { it.toEntity() }
            
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
            val entities = remoteLaunches.map { it.toEntity() }
            
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
            val entities = remoteLaunches.map { it.toEntity() }
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
            val entities = remoteLaunches.map { it.toEntity() }
            launchDao.deleteAllAndInsertUpcomingLaunches(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
    
    // Rocket implementations
    
    override fun getRockets(): Flow<Result<List<Rocket>>> {
        return rocketDao.getRockets()
            .onStart {
                // Trigger network refresh when flow starts
                refreshRocketsFromNetwork()
            }
            .map { entities ->
                val rockets = entities.map { it.toDomain() }
                Result.success(rockets)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getRocketById(id: String): Result<Rocket> {
        return try {
            // Try to get from local database first
            val localEntity = rocketDao.getRocketById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getRocketById(id)
            val entity = remoteDto.toEntity()
            rocketDao.insertRockets(listOf(entity))
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshRockets(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteRockets = apiService.getRockets()
            
            // Convert DTOs to entities
            val entities = remoteRockets.map { it.toEntity() }
            
            // Use transactional "delete all and insert new" strategy
            rocketDao.deleteAllAndInsertRockets(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh rockets from network without blocking the flow
     */
    private suspend fun refreshRocketsFromNetwork() {
        try {
            val remoteRockets = apiService.getRockets()
            val entities = remoteRockets.map { it.toEntity() }
            rocketDao.deleteAllAndInsertRockets(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
    
    // Capsule implementations
    
    override fun getCapsules(): Flow<Result<List<Capsule>>> {
        return capsuleDao.getCapsules()
            .onStart {
                // Trigger network refresh when flow starts
                refreshCapsulesFromNetwork()
            }
            .map { entities ->
                val capsules = entities.map { it.toDomain() }
                Result.success(capsules)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getCapsuleById(id: String): Result<Capsule> {
        return try {
            // Try to get from local database first
            val localEntity = capsuleDao.getCapsuleById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getCapsuleById(id)
            val entity = remoteDto.toEntity()
            capsuleDao.insertCapsules(listOf(entity))
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshCapsules(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteCapsules = apiService.getCapsules()
            
            // Convert DTOs to entities
            val entities = remoteCapsules.map { it.toEntity() }
            
            // Use transactional "delete all and insert new" strategy
            capsuleDao.deleteAllAndInsertCapsules(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh capsules from network without blocking the flow
     */
    private suspend fun refreshCapsulesFromNetwork() {
        try {
            val remoteCapsules = apiService.getCapsules()
            val entities = remoteCapsules.map { it.toEntity() }
            capsuleDao.deleteAllAndInsertCapsules(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
} 