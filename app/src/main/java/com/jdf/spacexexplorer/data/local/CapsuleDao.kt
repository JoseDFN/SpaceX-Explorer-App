package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.CapsuleEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for capsule database operations
 */
@Dao
interface CapsuleDao {
    
    /**
     * Get all capsules as a Flow for reactive updates
     */
    @Query("SELECT * FROM capsules ORDER BY serial ASC")
    fun getCapsules(): Flow<List<CapsuleEntity>>
    
    /**
     * Get a specific capsule by ID
     */
    @Query("SELECT * FROM capsules WHERE id = :id")
    suspend fun getCapsuleById(id: String): CapsuleEntity?
    
    /**
     * Insert or update capsules
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCapsules(capsules: List<CapsuleEntity>)
    
    /**
     * Delete all capsules
     */
    @Query("DELETE FROM capsules")
    suspend fun deleteAllCapsules()
    
    /**
     * Transactional method to delete all capsules and insert new ones
     */
    @androidx.room.Transaction
    suspend fun deleteAllAndInsertCapsules(capsules: List<CapsuleEntity>) {
        deleteAllCapsules()
        insertCapsules(capsules)
    }
    
    /**
     * Get active capsules only
     */
    @Query("SELECT * FROM capsules WHERE status = 'active' ORDER BY serial ASC")
    fun getActiveCapsules(): Flow<List<CapsuleEntity>>
    
    /**
     * Search capsules by serial, type, or status
     */
    @Query("SELECT * FROM capsules WHERE serial LIKE '%' || :query || '%' OR type LIKE '%' || :query || '%' OR status LIKE '%' || :query || '%' ORDER BY serial ASC")
    suspend fun searchCapsules(query: String): List<CapsuleEntity>
} 