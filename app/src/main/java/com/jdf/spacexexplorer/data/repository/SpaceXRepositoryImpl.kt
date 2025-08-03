package com.jdf.spacexexplorer.data.repository

import com.jdf.spacexexplorer.data.local.LaunchDao
import com.jdf.spacexexplorer.data.local.RocketDao
import com.jdf.spacexexplorer.data.local.CapsuleDao
import com.jdf.spacexexplorer.data.local.CoreDao
import com.jdf.spacexexplorer.data.local.CrewDao
import com.jdf.spacexexplorer.data.local.ShipDao
import com.jdf.spacexexplorer.data.mappers.toDomain
import com.jdf.spacexexplorer.data.mappers.toEntity
import com.jdf.spacexexplorer.data.remote.ApiService
import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.Capsule
import com.jdf.spacexexplorer.domain.model.Core
import com.jdf.spacexexplorer.domain.model.CrewMember
import com.jdf.spacexexplorer.domain.model.Ship
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
    private val capsuleDao: CapsuleDao,
    private val coreDao: CoreDao,
    private val crewDao: CrewDao,
    private val shipDao: ShipDao
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
    
    override suspend fun getLaunchesPage(page: Int, limit: Int): Result<List<Launch>> {
        return try {
            // Calculate offset from page number
            val offset = page * limit
            
            // Fetch from API with pagination
            val remoteLaunches = apiService.getLaunches(limit = limit, offset = offset)
            
            // Convert DTOs to domain models
            val launches = remoteLaunches.map { it.toDomain() }
            
            Result.success(launches)
        } catch (e: Exception) {
            Result.error(e)
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
    
    // Core implementations
    
    override fun getCores(): Flow<Result<List<Core>>> {
        return coreDao.getCores()
            .onStart {
                // Trigger network refresh when flow starts
                refreshCoresFromNetwork()
            }
            .map { entities ->
                val cores = entities.map { it.toDomain() }
                Result.success(cores)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getCoreById(id: String): Result<Core> {
        return try {
            // Try to get from local database first
            val localEntity = coreDao.getCoreById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getCoreById(id)
            val entity = remoteDto.toEntity()
            coreDao.insertCores(listOf(entity))
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshCores(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteCores = apiService.getCores()
            
            // Convert DTOs to entities
            val entities = remoteCores.map { it.toEntity() }
            
            // Use transactional "delete all and insert new" strategy
            coreDao.deleteAllAndInsertCores(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh cores from network without blocking the flow
     */
    private suspend fun refreshCoresFromNetwork() {
        try {
            val remoteCores = apiService.getCores()
            val entities = remoteCores.map { it.toEntity() }
            coreDao.deleteAllAndInsertCores(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
    
    // Crew implementations
    
    override fun getCrew(): Flow<Result<List<CrewMember>>> {
        return crewDao.getCrew()
            .onStart {
                // Trigger network refresh when flow starts
                refreshCrewFromNetwork()
            }
            .map { entities ->
                val crew = entities.map { it.toDomain() }
                Result.success(crew)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getCrewById(id: String): Result<CrewMember> {
        return try {
            // Try to get from local database first
            val localEntity = crewDao.getCrewById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getCrewById(id)
            val entity = remoteDto.toEntity()
            crewDao.insertCrew(listOf(entity))
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshCrew(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteCrew = apiService.getCrew()
            
            // Convert DTOs to entities
            val entities = remoteCrew.map { it.toEntity() }
            
            // Use transactional "delete all and insert new" strategy
            crewDao.deleteAllAndInsertCrew(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh crew from network without blocking the flow
     */
    private suspend fun refreshCrewFromNetwork() {
        try {
            val remoteCrew = apiService.getCrew()
            val entities = remoteCrew.map { it.toEntity() }
            crewDao.deleteAllAndInsertCrew(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
    
    // Ship implementations
    
    override fun getShips(): Flow<Result<List<Ship>>> {
        return shipDao.getShips()
            .onStart {
                // Trigger network refresh when flow starts
                refreshShipsFromNetwork()
            }
            .map { entities ->
                val ships = entities.map { it.toDomain() }
                Result.success(ships)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getShipById(id: String): Result<Ship> {
        return try {
            // Try to get from local database first
            val localEntity = shipDao.getShipById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getShipById(id)
            val entity = remoteDto.toEntity()
            shipDao.insertShips(listOf(entity))
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshShips(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteShips = apiService.getShips()
            
            // Convert DTOs to entities
            val entities = remoteShips.map { it.toEntity() }
            
            // Use transactional "delete all and insert new" strategy
            shipDao.deleteAllAndInsertShips(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh ships from network without blocking the flow
     */
    private suspend fun refreshShipsFromNetwork() {
        try {
            val remoteShips = apiService.getShips()
            val entities = remoteShips.map { it.toEntity() }
            shipDao.deleteAllAndInsertShips(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
} 