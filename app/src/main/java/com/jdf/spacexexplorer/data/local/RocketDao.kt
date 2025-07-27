package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.RocketEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for rocket database operations
 */
@Dao
interface RocketDao {
    
    /**
     * Get all rockets as a Flow for reactive updates
     */
    @Query("SELECT * FROM rockets ORDER BY name ASC")
    fun getRockets(): Flow<List<RocketEntity>>
    
    /**
     * Get a specific rocket by ID
     */
    @Query("SELECT * FROM rockets WHERE id = :id")
    suspend fun getRocketById(id: String): RocketEntity?
    
    /**
     * Insert or update rockets
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRockets(rockets: List<RocketEntity>)
    
    /**
     * Delete all rockets
     */
    @Query("DELETE FROM rockets")
    suspend fun deleteAllRockets()
    
    /**
     * Get active rockets only
     */
    @Query("SELECT * FROM rockets WHERE active = 1 ORDER BY name ASC")
    fun getActiveRockets(): Flow<List<RocketEntity>>
} 