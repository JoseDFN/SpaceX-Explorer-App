package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.ShipEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for ship database operations
 */
@Dao
interface ShipDao {

    /**
     * Get all ships as a Flow for reactive updates
     */
    @Query("SELECT * FROM ships ORDER BY name ASC")
    fun getShips(): Flow<List<ShipEntity>>

    /**
     * Get a specific ship by ID
     */
    @Query("SELECT * FROM ships WHERE id = :id")
    suspend fun getShipById(id: String): ShipEntity?

    /**
     * Insert or update ships
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShips(ships: List<ShipEntity>)

    /**
     * Delete all ships
     */
    @Query("DELETE FROM ships")
    suspend fun deleteAllShips()

    /**
     * Transactional method to delete all ships and insert new ones
     */
    @androidx.room.Transaction
    suspend fun deleteAllAndInsertShips(ships: List<ShipEntity>) {
        deleteAllShips()
        insertShips(ships)
    }

    /**
     * Get active ships only
     */
    @Query("SELECT * FROM ships WHERE active = 1 ORDER BY name ASC")
    fun getActiveShips(): Flow<List<ShipEntity>>
} 