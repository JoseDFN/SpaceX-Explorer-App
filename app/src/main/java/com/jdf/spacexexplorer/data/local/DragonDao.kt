package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.DragonEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for dragon database operations
 */
@Dao
interface DragonDao {

    /**
     * Get all dragons as a Flow for reactive updates
     */
    @Query("SELECT * FROM dragons ORDER BY name ASC")
    fun getDragons(): Flow<List<DragonEntity>>

    /**
     * Get a specific dragon by ID
     */
    @Query("SELECT * FROM dragons WHERE id = :id")
    suspend fun getDragonById(id: String): DragonEntity?

    /**
     * Insert or update dragons
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDragons(dragons: List<DragonEntity>)

    /**
     * Delete all dragons
     */
    @Query("DELETE FROM dragons")
    suspend fun deleteAllDragons()

    /**
     * Transactional method to delete all dragons and insert new ones
     */
    @androidx.room.Transaction
    suspend fun deleteAllAndInsertDragons(dragons: List<DragonEntity>) {
        deleteAllDragons()
        insertDragons(dragons)
    }

    /**
     * Get active dragons only
     */
    @Query("SELECT * FROM dragons WHERE active = 1 ORDER BY name ASC")
    fun getActiveDragons(): Flow<List<DragonEntity>>
    
    /**
     * Search dragons by name, type, or description
     */
    @Query("SELECT * FROM dragons WHERE name LIKE '%' || :query || '%' OR type LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun searchDragons(query: String): List<DragonEntity>

    @Query("DELETE FROM dragons")
    suspend fun clearAll()
} 