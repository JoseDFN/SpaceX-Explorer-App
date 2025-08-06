package com.jdf.spacexexplorer.domain.repository

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
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining the contract for SpaceX data operations.
 * This is part of the domain layer and defines what data the application needs.
 */
interface SpaceXRepository {
    
    /**
     * Get all launches as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getLaunches(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.DATE_DESC): Flow<Result<List<Launch>>>
    
    /**
     * Get launches with pagination support
     */
    suspend fun getLaunchesPage(page: Int, limit: Int = 20): Result<List<Launch>>
    
    /**
     * Get upcoming launches as a Flow with Result wrapper
     */
    fun getUpcomingLaunches(): Flow<Result<List<Launch>>>
    
    /**
     * Get past launches with optional limit and Result wrapper
     */
    fun getPastLaunches(limit: Int = 20): Flow<Result<List<Launch>>>
    
    /**
     * Get a specific launch by ID with Result wrapper
     */
    suspend fun getLaunchById(id: String): Result<Launch>
    
    /**
     * Get successful launches with optional limit and Result wrapper
     */
    fun getSuccessfulLaunches(limit: Int = 10): Flow<Result<List<Launch>>>
    
    /**
     * Refresh launches from the remote API
     */
    suspend fun refreshLaunches(): Result<Unit>
    
    /**
     * Refresh upcoming launches from the remote API
     */
    suspend fun refreshUpcomingLaunches(): Result<Unit>
    
    /**
     * Get all rockets as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getRockets(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Rocket>>>
    
    /**
     * Get a specific rocket by ID with Result wrapper
     */
    suspend fun getRocketById(id: String): Result<Rocket>
    
    /**
     * Refresh rockets from the remote API
     */
    suspend fun refreshRockets(): Result<Unit>
    
    /**
     * Get all capsules as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getCapsules(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Capsule>>>
    
    /**
     * Get a specific capsule by ID with Result wrapper
     */
    suspend fun getCapsuleById(id: String): Result<Capsule>
    
    /**
     * Refresh capsules from the remote API
     */
    suspend fun refreshCapsules(): Result<Unit>
    
    /**
     * Get all cores as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getCores(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Core>>>
    
    /**
     * Get a specific core by ID with Result wrapper
     */
    suspend fun getCoreById(id: String): Result<Core>
    
    /**
     * Refresh cores from the remote API
     */
    suspend fun refreshCores(): Result<Unit>
    
    /**
     * Get all crew members as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getCrew(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<CrewMember>>>
    
    /**
     * Get a specific crew member by ID with Result wrapper
     */
    suspend fun getCrewById(id: String): Result<CrewMember>
    
    /**
     * Refresh crew members from the remote API
     */
    suspend fun refreshCrew(): Result<Unit>
    
    /**
     * Get all ships as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getShips(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Ship>>>
    
    /**
     * Get a specific ship by ID with Result wrapper
     */
    suspend fun getShipById(id: String): Result<Ship>
    
    /**
     * Refresh ships from the remote API
     */
    suspend fun refreshShips(): Result<Unit>
    
    /**
     * Get all dragons as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getDragons(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Dragon>>>
    
    /**
     * Get a specific dragon by ID with Result wrapper
     */
    suspend fun getDragonById(id: String): Result<Dragon>
    
    /**
     * Refresh dragons from the remote API
     */
    suspend fun refreshDragons(): Result<Unit>
    
    /**
     * Get all landpads as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getLandpads(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Landpad>>>
    
    /**
     * Get a specific landpad by ID with Result wrapper
     */
    suspend fun getLandpadById(id: String): Result<Landpad>
    
    /**
     * Refresh landpads from the remote API
     */
    suspend fun refreshLandpads(): Result<Unit>
    
    /**
     * Get all launchpads as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getLaunchpads(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Launchpad>>>
    
    /**
     * Get a specific launchpad by ID with Result wrapper
     */
    suspend fun getLaunchpadById(id: String): Result<Launchpad>
    
    /**
     * Refresh launchpads from the remote API
     */
    suspend fun refreshLaunchpads(): Result<Unit>
    
    /**
     * Get all payloads as a Flow for reactive updates with Result wrapper
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     */
    fun getPayloads(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Payload>>>
    
    /**
     * Get a specific payload by ID with Result wrapper
     */
    suspend fun getPayloadById(id: String): Result<Payload>
    
    /**
     * Refresh payloads from the remote API
     */
    suspend fun refreshPayloads(): Result<Unit>
} 