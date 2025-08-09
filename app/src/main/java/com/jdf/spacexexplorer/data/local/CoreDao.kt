package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.CoreEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for core database operations
 */
@Dao
interface CoreDao {
    
    /**
     * Get all cores as a Flow for reactive updates
     */
    @Query("SELECT * FROM cores ORDER BY serial ASC")
    fun getCores(): Flow<List<CoreEntity>>
    
    /**
     * Get a specific core by ID
     */
    @Query("SELECT * FROM cores WHERE id = :id")
    suspend fun getCoreById(id: String): CoreEntity?
    
    /**
     * Insert or update cores
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCores(cores: List<CoreEntity>)
    
    /**
     * Delete all cores
     */
    @Query("DELETE FROM cores")
    suspend fun deleteAllCores()
    
    /**
     * Transactional method to delete all cores and insert new ones
     */
    @androidx.room.Transaction
    suspend fun deleteAllAndInsertCores(cores: List<CoreEntity>) {
        deleteAllCores()
        insertCores(cores)
    }
    
    /**
     * Get active cores only
     */
    @Query("SELECT * FROM cores WHERE status = 'active' ORDER BY serial ASC")
    fun getActiveCores(): Flow<List<CoreEntity>>
    
    /**
     * Search cores by serial, status, or block
     */
    @Query("SELECT * FROM cores WHERE serial LIKE '%' || :query || '%' OR status LIKE '%' || :query || '%' OR block LIKE '%' || :query || '%' ORDER BY serial ASC")
    suspend fun searchCores(query: String): List<CoreEntity>

    @Query("DELETE FROM cores")
    suspend fun clearAll()
} 