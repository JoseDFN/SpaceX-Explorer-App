package com.jdf.spacexexplorer.data.repository

import com.jdf.spacexexplorer.data.local.LaunchDao
import com.jdf.spacexexplorer.data.local.RocketDao
import com.jdf.spacexexplorer.data.local.CapsuleDao
import com.jdf.spacexexplorer.data.local.CoreDao
import com.jdf.spacexexplorer.data.local.CrewDao
import com.jdf.spacexexplorer.data.local.ShipDao
import com.jdf.spacexexplorer.data.local.DragonDao
import com.jdf.spacexexplorer.data.local.LandpadDao
import com.jdf.spacexexplorer.data.local.LaunchpadDao
import com.jdf.spacexexplorer.data.local.PayloadDao
import com.jdf.spacexexplorer.data.mappers.toDomain
import com.jdf.spacexexplorer.data.mappers.toEntity
import com.jdf.spacexexplorer.data.remote.ApiService
import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.Capsule
import com.jdf.spacexexplorer.domain.model.Core
import com.jdf.spacexexplorer.domain.model.CrewMember
import com.jdf.spacexexplorer.domain.model.Ship
import com.jdf.spacexexplorer.domain.model.Dragon
import com.jdf.spacexexplorer.domain.model.Landpad
import com.jdf.spacexexplorer.domain.model.Launchpad
import com.jdf.spacexexplorer.domain.model.Payload
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.model.SearchResult
import com.jdf.spacexexplorer.domain.model.Theme
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
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
    private val shipDao: ShipDao,
    private val dragonDao: DragonDao,
    private val landpadDao: LandpadDao,
    private val launchpadDao: LaunchpadDao,
    private val payloadDao: PayloadDao,
    private val dataStore: DataStore<Preferences>
) : SpaceXRepository {
    private companion object {
        val THEME_KEY = stringPreferencesKey("theme")
    }
    
    override suspend fun clearAllCaches() {
        // Clear every DAO table
        launchDao.clearAll()
        rocketDao.clearAll()
        capsuleDao.clearAll()
        coreDao.clearAll()
        crewDao.clearAll()
        shipDao.clearAll()
        dragonDao.clearAll()
        landpadDao.clearAll()
        launchpadDao.clearAll()
        payloadDao.clearAll()
    }

    override fun getTheme(): Flow<Theme> {
        return dataStore.data
            .map { preferences ->
                when (preferences[THEME_KEY]) {
                    Theme.DARK.name -> Theme.DARK
                    Theme.DARK_PURPLE.name -> Theme.DARK_PURPLE
                    Theme.LIGHT.name -> Theme.LIGHT
                    else -> Theme.LIGHT
                }
            }
    }

    override suspend fun setTheme(theme: Theme) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.name
        }
    }
    
    override fun getLaunches(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<Launch>>> {
        return getFilteredLaunchesFlow(filters)
            .map { entities ->
                val launches = entities.map { it.toDomain() }
                val sortedLaunches = applySorting(launches, sort)
                Result.success(sortedLaunches)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override fun getUpcomingLaunches(): Flow<Result<List<Launch>>> {
        return launchDao.getUpcomingLaunches()
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
    
    override fun getRockets(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<Rocket>>> {
        return rocketDao.getRockets()
            .map { entities ->
                val rockets = entities.map { it.toDomain() }
                val filteredRockets = applyRocketFilters(rockets, filters)
                val sortedRockets = applyRocketSorting(filteredRockets, sort)
                Result.success(sortedRockets)
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
    
    override fun getCapsules(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<Capsule>>> {
        return capsuleDao.getCapsules()
            .map { entities ->
                val capsules = entities.map { it.toDomain() }
                val filteredCapsules = applyCapsuleFilters(capsules, filters)
                val sortedCapsules = applyCapsuleSorting(filteredCapsules, sort)
                Result.success(sortedCapsules)
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
    
    override fun getCores(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<Core>>> {
        return coreDao.getCores()
            .map { entities ->
                val cores = entities.map { it.toDomain() }
                val filteredCores = applyCoreFilters(cores, filters)
                val sortedCores = applyCoreSorting(filteredCores, sort)
                Result.success(sortedCores)
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
    
    override fun getCrew(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<CrewMember>>> {
        return crewDao.getCrew()
            .map { entities ->
                val crew = entities.map { it.toDomain() }
                val filteredCrew = applyCrewFilters(crew, filters)
                val sortedCrew = applyCrewSorting(filteredCrew, sort)
                Result.success(sortedCrew)
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
    
    override fun getShips(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<Ship>>> {
        return shipDao.getShips()
            .map { entities ->
                val ships = entities.map { it.toDomain() }
                val filteredShips = applyShipFilters(ships, filters)
                val sortedShips = applyShipSorting(filteredShips, sort)
                Result.success(sortedShips)
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
    
    // Dragon implementations
    
    override fun getDragons(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<Dragon>>> {
        return dragonDao.getDragons()
            .map { entities ->
                val dragons = entities.map { it.toDomain() }
                val filteredDragons = applyDragonFilters(dragons, filters)
                val sortedDragons = applyDragonSorting(filteredDragons, sort)
                Result.success(sortedDragons)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getDragonById(id: String): Result<Dragon> {
        return try {
            // Try to get from local database first
            val localEntity = dragonDao.getDragonById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getDragonById(id)
            val entity = remoteDto.toEntity()
            dragonDao.insertDragons(listOf(entity))
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshDragons(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteDragons = apiService.getDragons()
            
            // Convert DTOs to entities
            val entities = remoteDragons.map { it.toEntity() }
            
            // Use transactional "delete all and insert new" strategy
            dragonDao.deleteAllAndInsertDragons(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh dragons from network without blocking the flow
     */
    private suspend fun refreshDragonsFromNetwork() {
        try {
            val remoteDragons = apiService.getDragons()
            val entities = remoteDragons.map { it.toEntity() }
            dragonDao.deleteAllAndInsertDragons(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
    
    // Landpad implementations
    
    override fun getLandpads(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<Landpad>>> {
        return landpadDao.getLandpads()
            .map { entities ->
                val landpads = entities.map { it.toDomain() }
                val filteredLandpads = applyLandpadFilters(landpads, filters)
                val sortedLandpads = applyLandpadSorting(filteredLandpads, sort)
                Result.success(sortedLandpads)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getLandpadById(id: String): Result<Landpad> {
        return try {
            // Try to get from local database first
            val localEntity = landpadDao.getLandpadById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getLandpadById(id)
            val entity = remoteDto.toEntity()
            landpadDao.insertLandpads(listOf(entity))
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshLandpads(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteLandpads = apiService.getLandpads()
            
            // Convert DTOs to entities
            val entities = remoteLandpads.map { it.toEntity() }
            
            // Use transactional "delete all and insert new" strategy
            landpadDao.deleteAllAndInsertLandpads(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh landpads from network without blocking the flow
     */
    private suspend fun refreshLandpadsFromNetwork() {
        try {
            val remoteLandpads = apiService.getLandpads()
            val entities = remoteLandpads.map { it.toEntity() }
            landpadDao.deleteAllAndInsertLandpads(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
    
    // Launchpad implementations
    
    override fun getLaunchpads(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<Launchpad>>> {
        return launchpadDao.getLaunchpads()
            .map { entities ->
                val launchpads = entities.map { it.toDomain() }
                val filteredLaunchpads = applyLaunchpadFilters(launchpads, filters)
                val sortedLaunchpads = applyLaunchpadSorting(filteredLaunchpads, sort)
                Result.success(sortedLaunchpads)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getLaunchpadById(id: String): Result<Launchpad> {
        return try {
            // Try to get from local database first
            val localEntity = launchpadDao.getLaunchpadById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getLaunchpadById(id)
            val entity = remoteDto.toEntity()
            launchpadDao.insertLaunchpads(listOf(entity))
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshLaunchpads(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteLaunchpads = apiService.getLaunchpads()
            
            // Convert DTOs to entities
            val entities = remoteLaunchpads.map { it.toEntity() }
            
            // Use transactional "delete all and insert new" strategy
            launchpadDao.deleteAllAndInsertLaunchpads(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh launchpads from network without blocking the flow
     */
    private suspend fun refreshLaunchpadsFromNetwork() {
        try {
            val remoteLaunchpads = apiService.getLaunchpads()
            val entities = remoteLaunchpads.map { it.toEntity() }
            launchpadDao.deleteAllAndInsertLaunchpads(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
    
    // Payload implementations
    
    override fun getPayloads(filters: List<FilterOption>, sort: SortOption): Flow<Result<List<Payload>>> {
        return payloadDao.getPayloads()
            .map { entities ->
                val payloads = entities.map { it.toDomain() }
                val filteredPayloads = applyPayloadFilters(payloads, filters)
                val sortedPayloads = applyPayloadSorting(filteredPayloads, sort)
                Result.success(sortedPayloads)
            }
            .catch { e ->
                emit(Result.error(Exception(e)))
            }
    }
    
    override suspend fun getPayloadById(id: String): Result<Payload> {
        return try {
            // Try to get from local database first
            val localEntity = payloadDao.getPayloadById(id)
            if (localEntity != null) {
                return Result.success(localEntity.toDomain())
            }
            // If not found, fetch from API
            val remoteDto = apiService.getPayloadById(id)
            val entity = remoteDto.toEntity()
            payloadDao.insertPayloads(listOf(entity))
            Result.success(entity.toDomain())
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshPayloads(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remotePayloads = apiService.getPayloads()
            
            // Convert DTOs to entities
            val entities = remotePayloads.map { it.toEntity() }
            
            // Use transactional "delete all and insert new" strategy
            payloadDao.deleteAllAndInsertPayloads(entities)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    /**
     * Private method to refresh payloads from network without blocking the flow
     */
    private suspend fun refreshPayloadsFromNetwork() {
        try {
            val remotePayloads = apiService.getPayloads()
            val entities = remotePayloads.map { it.toEntity() }
            payloadDao.deleteAllAndInsertPayloads(entities)
        } catch (e: Exception) {
            // Silently handle network errors - local data will still be emitted
            // The error will be handled by the UI layer if needed
        }
    }
    
    /**
     * Helper method to get the appropriate Flow based on filters
     */
    private fun getFilteredLaunchesFlow(filters: List<FilterOption>): Flow<List<com.jdf.spacexexplorer.data.local.entity.LaunchEntity>> {
        return when {
            filters.isEmpty() -> {
                launchDao.getAllLaunches()
            }
            else -> {
                // For any combination of filters, get all launches and apply filters in memory
                launchDao.getAllLaunches().map { launches ->
                    val filteredLaunches = launches.filter { launch ->
                        filters.all { filter ->
                            when (filter) {
                                is FilterOption.LaunchYearFilter -> {
                                    val launchYear = java.time.Instant.ofEpochSecond(launch.launchDateUnix)
                                        .atZone(java.time.ZoneOffset.UTC)
                                        .year
                                    launchYear == filter.year
                                }
                                is FilterOption.LaunchSuccessFilter -> launch.wasSuccessful == filter.successful
                                is FilterOption.LaunchUpcomingFilter -> launch.isUpcoming == filter.upcoming
                                is FilterOption.LaunchRocketFilter -> launch.rocketId == filter.rocketId
                                is FilterOption.LaunchDateRangeFilter -> {
                                    launch.launchDateUnix >= filter.startDate && launch.launchDateUnix <= filter.endDate
                                }
                                else -> true // Ignore non-launch filters
                            }
                        }
                    }
                    filteredLaunches
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to the list of launches
     */
    private fun applySorting(launches: List<Launch>, sort: SortOption): List<Launch> {
        return when (sort) {
            SortOption.DATE_DESC -> launches.sortedByDescending { it.launchDateUnix }
            SortOption.DATE_ASC -> launches.sortedBy { it.launchDateUnix }
            SortOption.NAME_ASC -> launches.sortedBy { it.missionName }
            SortOption.NAME_DESC -> launches.sortedByDescending { it.missionName }
            SortOption.FLIGHT_NUMBER_ASC -> launches.sortedBy { it.flightNumber }
            SortOption.FLIGHT_NUMBER_DESC -> launches.sortedByDescending { it.flightNumber }
            else -> launches // Default to original order
        }
    }
    
    /**
     * Extension function to find a specific filter type in the list
     */
    private inline fun <reified T : FilterOption> List<FilterOption>.findIsInstance(): T? {
        return this.find { it is T } as? T
    }
    
    /**
     * Helper method to apply filters to rockets
     */
    private fun applyRocketFilters(rockets: List<Rocket>, filters: List<FilterOption>): List<Rocket> {
        return rockets.filter { rocket ->
            filters.all { filter ->
                when (filter) {
                    is FilterOption.RocketActiveFilter -> rocket.active == filter.active
                    is FilterOption.RocketTypeFilter -> rocket.type == filter.type
                    else -> true // Ignore non-rocket filters
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to rockets
     */
    private fun applyRocketSorting(rockets: List<Rocket>, sort: SortOption): List<Rocket> {
        return when (sort) {
            SortOption.NAME_ASC -> rockets.sortedBy { it.name }
            SortOption.NAME_DESC -> rockets.sortedByDescending { it.name }
            SortOption.ROCKET_NAME_ASC -> rockets.sortedBy { it.name }
            SortOption.ROCKET_NAME_DESC -> rockets.sortedByDescending { it.name }
            else -> rockets // Default to original order
        }
    }
    
    /**
     * Helper method to apply filters to capsules
     */
    private fun applyCapsuleFilters(capsules: List<Capsule>, filters: List<FilterOption>): List<Capsule> {
        return capsules.filter { capsule ->
            filters.all { filter ->
                when (filter) {
                    is FilterOption.CapsuleTypeFilter -> capsule.type == filter.type
                    is FilterOption.CapsuleStatusFilter -> capsule.status == filter.status
                    else -> true // Ignore non-capsule filters
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to capsules
     */
    private fun applyCapsuleSorting(capsules: List<Capsule>, sort: SortOption): List<Capsule> {
        return when (sort) {
            SortOption.NAME_ASC -> capsules.sortedBy { it.serial }
            SortOption.NAME_DESC -> capsules.sortedByDescending { it.serial }
            SortOption.CAPSULE_SERIAL_ASC -> capsules.sortedBy { it.serial }
            SortOption.CAPSULE_SERIAL_DESC -> capsules.sortedByDescending { it.serial }
            else -> capsules // Default to original order
        }
    }
    
    /**
     * Helper method to apply filters to cores
     */
    private fun applyCoreFilters(cores: List<Core>, filters: List<FilterOption>): List<Core> {
        return cores.filter { core ->
            filters.all { filter ->
                when (filter) {
                    is FilterOption.CoreStatusFilter -> core.status == filter.status
                    is FilterOption.CoreBlockFilter -> core.block == filter.block
                    else -> true // Ignore non-core filters
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to cores
     */
    private fun applyCoreSorting(cores: List<Core>, sort: SortOption): List<Core> {
        return when (sort) {
            SortOption.NAME_ASC -> cores.sortedBy { it.serial }
            SortOption.NAME_DESC -> cores.sortedByDescending { it.serial }
            SortOption.CORE_SERIAL_ASC -> cores.sortedBy { it.serial }
            SortOption.CORE_SERIAL_DESC -> cores.sortedByDescending { it.serial }
            else -> cores // Default to original order
        }
    }
    
    /**
     * Helper method to apply filters to crew
     */
    private fun applyCrewFilters(crew: List<CrewMember>, filters: List<FilterOption>): List<CrewMember> {
        return crew.filter { crewMember ->
            filters.all { filter ->
                when (filter) {
                    is FilterOption.CrewAgencyFilter -> crewMember.agency == filter.agency
                    is FilterOption.CrewStatusFilter -> crewMember.status == filter.status
                    else -> true // Ignore non-crew filters
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to crew
     */
    private fun applyCrewSorting(crew: List<CrewMember>, sort: SortOption): List<CrewMember> {
        return when (sort) {
            SortOption.NAME_ASC -> crew.sortedBy { it.name }
            SortOption.NAME_DESC -> crew.sortedByDescending { it.name }
            SortOption.CREW_NAME_ASC -> crew.sortedBy { it.name }
            SortOption.CREW_NAME_DESC -> crew.sortedByDescending { it.name }
            else -> crew // Default to original order
        }
    }
    
    /**
     * Helper method to apply filters to ships
     */
    private fun applyShipFilters(ships: List<Ship>, filters: List<FilterOption>): List<Ship> {
        return ships.filter { ship ->
            filters.all { filter ->
                when (filter) {
                    is FilterOption.ShipActiveFilter -> ship.active == filter.active
                    is FilterOption.ShipTypeFilter -> ship.type == filter.type
                    else -> true // Ignore non-ship filters
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to ships
     */
    private fun applyShipSorting(ships: List<Ship>, sort: SortOption): List<Ship> {
        return when (sort) {
            SortOption.NAME_ASC -> ships.sortedBy { it.name }
            SortOption.NAME_DESC -> ships.sortedByDescending { it.name }
            SortOption.SHIP_NAME_ASC -> ships.sortedBy { it.name }
            SortOption.SHIP_NAME_DESC -> ships.sortedByDescending { it.name }
            else -> ships // Default to original order
        }
    }
    
    /**
     * Helper method to apply filters to dragons
     */
    private fun applyDragonFilters(dragons: List<Dragon>, filters: List<FilterOption>): List<Dragon> {
        return dragons.filter { dragon ->
            filters.all { filter ->
                when (filter) {
                    is FilterOption.DragonActiveFilter -> dragon.active == filter.active
                    is FilterOption.DragonTypeFilter -> dragon.type == filter.type
                    else -> true // Ignore non-dragon filters
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to dragons
     */
    private fun applyDragonSorting(dragons: List<Dragon>, sort: SortOption): List<Dragon> {
        return when (sort) {
            SortOption.NAME_ASC -> dragons.sortedBy { it.name }
            SortOption.NAME_DESC -> dragons.sortedByDescending { it.name }
            SortOption.DRAGON_NAME_ASC -> dragons.sortedBy { it.name }
            SortOption.DRAGON_NAME_DESC -> dragons.sortedByDescending { it.name }
            else -> dragons // Default to original order
        }
    }
    
    /**
     * Helper method to apply filters to landpads
     */
    private fun applyLandpadFilters(landpads: List<Landpad>, filters: List<FilterOption>): List<Landpad> {
        return landpads.filter { landpad ->
            filters.all { filter ->
                when (filter) {
                    is FilterOption.LandpadStatusFilter -> landpad.status == filter.status
                    is FilterOption.LandpadTypeFilter -> landpad.type == filter.type
                    else -> true // Ignore non-landpad filters
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to landpads
     */
    private fun applyLandpadSorting(landpads: List<Landpad>, sort: SortOption): List<Landpad> {
        return when (sort) {
            SortOption.NAME_ASC -> landpads.sortedBy { it.name }
            SortOption.NAME_DESC -> landpads.sortedByDescending { it.name }
            SortOption.LANDPAD_NAME_ASC -> landpads.sortedBy { it.name }
            SortOption.LANDPAD_NAME_DESC -> landpads.sortedByDescending { it.name }
            else -> landpads // Default to original order
        }
    }
    
    /**
     * Helper method to apply filters to launchpads
     */
    private fun applyLaunchpadFilters(launchpads: List<Launchpad>, filters: List<FilterOption>): List<Launchpad> {
        return launchpads.filter { launchpad ->
            filters.all { filter ->
                when (filter) {
                    is FilterOption.LaunchpadStatusFilter -> launchpad.status == filter.status
                    is FilterOption.LaunchpadRegionFilter -> launchpad.region == filter.region
                    else -> true // Ignore non-launchpad filters
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to launchpads
     */
    private fun applyLaunchpadSorting(launchpads: List<Launchpad>, sort: SortOption): List<Launchpad> {
        return when (sort) {
            SortOption.NAME_ASC -> launchpads.sortedBy { it.name }
            SortOption.NAME_DESC -> launchpads.sortedByDescending { it.name }
            SortOption.LAUNCHPAD_NAME_ASC -> launchpads.sortedBy { it.name }
            SortOption.LAUNCHPAD_NAME_DESC -> launchpads.sortedByDescending { it.name }
            else -> launchpads // Default to original order
        }
    }
    
    /**
     * Helper method to apply filters to payloads
     */
    private fun applyPayloadFilters(payloads: List<Payload>, filters: List<FilterOption>): List<Payload> {
        return payloads.filter { payload ->
            filters.all { filter ->
                when (filter) {
                    is FilterOption.PayloadTypeFilter -> payload.type == filter.type
                    is FilterOption.PayloadNationalityFilter -> payload.nationalities.contains(filter.nationality)
                    else -> true // Ignore non-payload filters
                }
            }
        }
    }
    
    /**
     * Helper method to apply sorting to payloads
     */
    private fun applyPayloadSorting(payloads: List<Payload>, sort: SortOption): List<Payload> {
        return when (sort) {
            SortOption.NAME_ASC -> payloads.sortedBy { it.name }
            SortOption.NAME_DESC -> payloads.sortedByDescending { it.name }
            SortOption.PAYLOAD_NAME_ASC -> payloads.sortedBy { it.name }
            SortOption.PAYLOAD_NAME_DESC -> payloads.sortedByDescending { it.name }
            else -> payloads // Default to original order
        }
    }
    
    override suspend fun searchAll(query: String): Result<List<SearchResult>> {
        return try {
            coroutineScope {
                // Search all entities in parallel
                val launchResults = async { launchDao.searchLaunches(query) }
                val rocketResults = async { rocketDao.searchRockets(query) }
                val capsuleResults = async { capsuleDao.searchCapsules(query) }
                val coreResults = async { coreDao.searchCores(query) }
                val crewResults = async { crewDao.searchCrew(query) }
                val shipResults = async { shipDao.searchShips(query) }
                val dragonResults = async { dragonDao.searchDragons(query) }
                val landpadResults = async { landpadDao.searchLandpads(query) }
                val launchpadResults = async { launchpadDao.searchLaunchpads(query) }
                val payloadResults = async { payloadDao.searchPayloads(query) }
                
                // Wait for all results and map them to SearchResult types
                val searchResults = mutableListOf<SearchResult>()
                
                // Add launch results
                searchResults.addAll(launchResults.await().map { SearchResult.LaunchResult(it.toDomain()) })
                
                // Add rocket results
                searchResults.addAll(rocketResults.await().map { SearchResult.RocketResult(it.toDomain()) })
                
                // Add capsule results
                searchResults.addAll(capsuleResults.await().map { SearchResult.CapsuleResult(it.toDomain()) })
                
                // Add core results
                searchResults.addAll(coreResults.await().map { SearchResult.CoreResult(it.toDomain()) })
                
                // Add crew results
                searchResults.addAll(crewResults.await().map { SearchResult.CrewResult(it.toDomain()) })
                
                // Add ship results
                searchResults.addAll(shipResults.await().map { SearchResult.ShipResult(it.toDomain()) })
                
                // Add dragon results
                searchResults.addAll(dragonResults.await().map { SearchResult.DragonResult(it.toDomain()) })
                
                // Add landpad results
                searchResults.addAll(landpadResults.await().map { SearchResult.LandpadResult(it.toDomain()) })
                
                // Add launchpad results
                searchResults.addAll(launchpadResults.await().map { SearchResult.LaunchpadResult(it.toDomain()) })
                
                // Add payload results
                searchResults.addAll(payloadResults.await().map { SearchResult.PayloadResult(it.toDomain()) })
                
                Result.success(searchResults)
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }
} 