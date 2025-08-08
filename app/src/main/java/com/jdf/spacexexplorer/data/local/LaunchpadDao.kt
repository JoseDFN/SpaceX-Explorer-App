package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.LaunchpadEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for launchpad database operations
 */
@Dao
interface LaunchpadDao {
    
    /**
     * Get all launchpads as a Flow for reactive updates
     */
    @Query("SELECT * FROM launchpads ORDER BY name ASC")
    fun getLaunchpads(): Flow<List<LaunchpadEntity>>
    
    /**
     * Get a specific launchpad by ID
     */
    @Query("SELECT * FROM launchpads WHERE id = :id")
    suspend fun getLaunchpadById(id: String): LaunchpadEntity?
    
    /**
     * Insert or update launchpads
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunchpads(launchpads: List<LaunchpadEntity>)
    
    /**
     * Delete all launchpads
     */
    @Query("DELETE FROM launchpads")
    suspend fun deleteAllLaunchpads()
    
    /**
     * Transactional method to delete all launchpads and insert new ones
     */
    @androidx.room.Transaction
    suspend fun deleteAllAndInsertLaunchpads(launchpads: List<LaunchpadEntity>) {
        deleteAllLaunchpads()
        insertLaunchpads(launchpads)
    }
    
    /**
     * Get active launchpads only
     */
    @Query("SELECT * FROM launchpads WHERE status = 'active' ORDER BY name ASC")
    fun getActiveLaunchpads(): Flow<List<LaunchpadEntity>>
    
    /**
     * Search launchpads by name, full name, or locality
     */
    @Query("SELECT * FROM launchpads WHERE name LIKE '%' || :query || '%' OR fullName LIKE '%' || :query || '%' OR locality LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun searchLaunchpads(query: String): List<LaunchpadEntity>
} 