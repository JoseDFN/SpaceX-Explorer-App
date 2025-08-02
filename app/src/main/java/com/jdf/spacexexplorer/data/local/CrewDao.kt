package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.CrewEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for crew database operations
 */
@Dao
interface CrewDao {
    
    /**
     * Get all crew members as a Flow for reactive updates
     */
    @Query("SELECT * FROM crew ORDER BY name ASC")
    fun getCrew(): Flow<List<CrewEntity>>
    
    /**
     * Get a specific crew member by ID
     */
    @Query("SELECT * FROM crew WHERE id = :id")
    suspend fun getCrewById(id: String): CrewEntity?
    
    /**
     * Insert or update crew members
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrew(crew: List<CrewEntity>)
    
    /**
     * Delete all crew members
     */
    @Query("DELETE FROM crew")
    suspend fun deleteAllCrew()
    
    /**
     * Transactional method to delete all crew members and insert new ones
     */
    @androidx.room.Transaction
    suspend fun deleteAllAndInsertCrew(crew: List<CrewEntity>) {
        deleteAllCrew()
        insertCrew(crew)
    }
    
    /**
     * Get active crew members only
     */
    @Query("SELECT * FROM crew WHERE status = 'active' ORDER BY name ASC")
    fun getActiveCrew(): Flow<List<CrewEntity>>
} 