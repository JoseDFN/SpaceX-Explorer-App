package com.jdf.spacexexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdf.spacexexplorer.data.local.entity.LaunchEntity
import com.jdf.spacexexplorer.data.local.entity.RocketEntity
import com.jdf.spacexexplorer.data.local.entity.CapsuleEntity

@Database(
    entities = [LaunchEntity::class, RocketEntity::class, CapsuleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao
    abstract fun rocketDao(): RocketDao
    abstract fun capsuleDao(): CapsuleDao
} 