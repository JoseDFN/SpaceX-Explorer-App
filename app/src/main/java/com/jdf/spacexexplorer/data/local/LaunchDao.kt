package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jdf.spacexexplorer.data.local.entity.LaunchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchDao {
    
    @Query("SELECT * FROM launches ORDER BY launchDateUnix DESC")
    fun getAllLaunches(): Flow<List<LaunchEntity>>
    
    @Query("SELECT * FROM launches WHERE isUpcoming = 1 ORDER BY launchDateUnix ASC")
    fun getUpcomingLaunches(): Flow<List<LaunchEntity>>
    
    @Query("SELECT * FROM launches WHERE isUpcoming = 0 ORDER BY launchDateUnix DESC LIMIT :limit")
    fun getPastLaunches(limit: Int = 20): Flow<List<LaunchEntity>>
    
    @Query("SELECT * FROM launches WHERE id = :launchId")
    suspend fun getLaunchById(launchId: String): LaunchEntity?
    
    @Query("SELECT * FROM launches WHERE wasSuccessful = 1 ORDER BY launchDateUnix DESC LIMIT :limit")
    fun getSuccessfulLaunches(limit: Int = 10): Flow<List<LaunchEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunch(launch: LaunchEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunches(launches: List<LaunchEntity>)
    
    @Query("DELETE FROM launches")
    suspend fun deleteAllLaunches()
    
    @Query("DELETE FROM launches WHERE id = :launchId")
    suspend fun deleteLaunchById(launchId: String)
    
    /**
     * Transaction to delete all launches and insert new ones
     * This ensures data consistency during refresh operations
     */
    @Transaction
    suspend fun deleteAllAndInsertLaunches(launches: List<LaunchEntity>) {
        deleteAllLaunches()
        insertLaunches(launches)
    }
    
    /**
     * Transaction to delete upcoming launches and insert new ones
     */
    @Transaction
    suspend fun deleteAllAndInsertUpcomingLaunches(launches: List<LaunchEntity>) {
        // Delete only upcoming launches
        deleteUpcomingLaunches()
        insertLaunches(launches)
    }
    
    @Query("DELETE FROM launches WHERE isUpcoming = 1")
    suspend fun deleteUpcomingLaunches()
} 