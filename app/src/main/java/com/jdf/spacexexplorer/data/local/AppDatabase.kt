package com.jdf.spacexexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdf.spacexexplorer.data.local.entity.LaunchEntity
import com.jdf.spacexexplorer.data.local.entity.RocketEntity
import com.jdf.spacexexplorer.data.local.entity.CapsuleEntity
import com.jdf.spacexexplorer.data.local.entity.CoreEntity
import com.jdf.spacexexplorer.data.local.entity.CrewEntity
import com.jdf.spacexexplorer.data.local.entity.ShipEntity
import com.jdf.spacexexplorer.data.local.entity.DragonEntity

@Database(
    entities = [LaunchEntity::class, RocketEntity::class, CapsuleEntity::class, CoreEntity::class, CrewEntity::class, ShipEntity::class, DragonEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao
    abstract fun rocketDao(): RocketDao
    abstract fun capsuleDao(): CapsuleDao
    abstract fun coreDao(): CoreDao
    abstract fun crewDao(): CrewDao
    abstract fun shipDao(): ShipDao
    abstract fun dragonDao(): DragonDao
} 