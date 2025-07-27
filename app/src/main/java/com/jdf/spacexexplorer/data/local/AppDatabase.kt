package com.jdf.spacexexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdf.spacexexplorer.data.local.entity.LaunchEntity

@Database(
    entities = [LaunchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao
} 