package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.LaunchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchDao {
    
    @Query("SELECT * FROM launches ORDER BY dateUnix DESC")
    fun getAllLaunches(): Flow<List<LaunchEntity>>
    
    @Query("SELECT * FROM launches WHERE upcoming = 1 ORDER BY dateUnix ASC")
    fun getUpcomingLaunches(): Flow<List<LaunchEntity>>
    
    @Query("SELECT * FROM launches WHERE upcoming = 0 ORDER BY dateUnix DESC LIMIT :limit")
    fun getPastLaunches(limit: Int = 20): Flow<List<LaunchEntity>>
    
    @Query("SELECT * FROM launches WHERE id = :launchId")
    suspend fun getLaunchById(launchId: String): LaunchEntity?
    
    @Query("SELECT * FROM launches WHERE success = 1 ORDER BY dateUnix DESC LIMIT :limit")
    fun getSuccessfulLaunches(limit: Int = 10): Flow<List<LaunchEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunch(launch: LaunchEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunches(launches: List<LaunchEntity>)
    
    @Query("DELETE FROM launches")
    suspend fun deleteAllLaunches()
    
    @Query("DELETE FROM launches WHERE id = :launchId")
    suspend fun deleteLaunchById(launchId: String)
} 