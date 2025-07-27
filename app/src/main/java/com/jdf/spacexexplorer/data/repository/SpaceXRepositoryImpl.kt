package com.jdf.spacexexplorer.data.repository

import com.jdf.spacexexplorer.data.local.datasource.LocalDataSource
import com.jdf.spacexexplorer.data.mappers.toLaunch
import com.jdf.spacexexplorer.data.mappers.toLaunchEntity
import com.jdf.spacexexplorer.data.remote.datasource.RemoteDataSource
import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of SpaceXRepository that follows the single source of truth strategy.
 * The local database (Room) is the single source of truth for the UI.
 */
class SpaceXRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : SpaceXRepository {
    
    override fun getLaunches(): Flow<Result<List<Launch>>> {
        return localDataSource.getAllLaunches().map { result ->
            result.map { entities ->
                entities.map { it.toLaunch() }
            }
        }
    }
    
    override fun getUpcomingLaunches(): Flow<Result<List<Launch>>> {
        return localDataSource.getUpcomingLaunches().map { result ->
            result.map { entities ->
                entities.map { it.toLaunch() }
            }
        }
    }
    
    override fun getPastLaunches(limit: Int): Flow<Result<List<Launch>>> {
        return localDataSource.getPastLaunches(limit).map { result ->
            result.map { entities ->
                entities.map { it.toLaunch() }
            }
        }
    }
    
    override suspend fun getLaunchById(launchId: String): Result<Launch?> {
        return localDataSource.getLaunchById(launchId).map { entity ->
            entity?.toLaunch()
        }
    }
    
    override fun getSuccessfulLaunches(limit: Int): Flow<Result<List<Launch>>> {
        return localDataSource.getSuccessfulLaunches(limit).map { result ->
            result.map { entities ->
                entities.map { it.toLaunch() }
            }
        }
    }
    
    override suspend fun refreshLaunches(): Result<Unit> {
        return try {
            // Fetch fresh data from API
            val remoteResult = remoteDataSource.getLaunches()
            
            when (remoteResult) {
                is Result.Success -> {
                    // Convert DTOs to entities
                    val entities = remoteResult.data.map { it.toLaunchEntity() }
                    
                    // Delete all and insert new data (single source of truth strategy)
                    localDataSource.deleteAllAndInsertLaunches(entities)
                }
                is Result.Error -> {
                    Result.error(remoteResult.exception)
                }
                is Result.Loading -> {
                    Result.loading()
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    
    override suspend fun refreshUpcomingLaunches(): Result<Unit> {
        return try {
            // Fetch fresh upcoming launches from API
            val remoteResult = remoteDataSource.getUpcomingLaunches()
            
            when (remoteResult) {
                is Result.Success -> {
                    // Convert DTOs to entities
                    val entities = remoteResult.data.map { it.toLaunchEntity() }
                    
                    // Delete upcoming launches and insert new ones
                    localDataSource.deleteAllAndInsertUpcomingLaunches(entities)
                }
                is Result.Error -> {
                    Result.error(remoteResult.exception)
                }
                is Result.Loading -> {
                    Result.loading()
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }
} 