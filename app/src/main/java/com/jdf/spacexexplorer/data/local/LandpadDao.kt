package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.LandpadEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for landpad database operations
 */
@Dao
interface LandpadDao {
    
    /**
     * Get all landpads as a Flow for reactive updates
     */
    @Query("SELECT * FROM landpads ORDER BY name ASC")
    fun getLandpads(): Flow<List<LandpadEntity>>
    
    /**
     * Get a specific landpad by ID
     */
    @Query("SELECT * FROM landpads WHERE id = :id")
    suspend fun getLandpadById(id: String): LandpadEntity?
    
    /**
     * Insert or update landpads
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLandpads(landpads: List<LandpadEntity>)
    
    /**
     * Delete all landpads
     */
    @Query("DELETE FROM landpads")
    suspend fun deleteAllLandpads()
    
    /**
     * Transactional method to delete all landpads and insert new ones
     */
    @androidx.room.Transaction
    suspend fun deleteAllAndInsertLandpads(landpads: List<LandpadEntity>) {
        deleteAllLandpads()
        insertLandpads(landpads)
    }
    
    /**
     * Get active landpads only
     */
    @Query("SELECT * FROM landpads WHERE status = 'active' ORDER BY name ASC")
    fun getActiveLandpads(): Flow<List<LandpadEntity>>
} 