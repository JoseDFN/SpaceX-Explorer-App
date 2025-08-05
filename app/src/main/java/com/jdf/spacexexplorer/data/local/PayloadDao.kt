package com.jdf.spacexexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jdf.spacexexplorer.data.local.entity.PayloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PayloadDao {
    @Query("SELECT * FROM payloads ORDER BY name ASC")
    fun getPayloads(): Flow<List<PayloadEntity>>
    
    @Query("SELECT * FROM payloads WHERE id = :id")
    suspend fun getPayloadById(id: String): PayloadEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayloads(payloads: List<PayloadEntity>)
    
    @Query("DELETE FROM payloads")
    suspend fun deleteAllPayloads()
    
    @androidx.room.Transaction
    suspend fun deleteAllAndInsertPayloads(payloads: List<PayloadEntity>) {
        deleteAllPayloads()
        insertPayloads(payloads)
    }
    
    @Query("SELECT * FROM payloads WHERE type = :type ORDER BY name ASC")
    fun getPayloadsByType(type: String): Flow<List<PayloadEntity>>
    
    @Query("SELECT * FROM payloads WHERE reused = 1 ORDER BY name ASC")
    fun getReusedPayloads(): Flow<List<PayloadEntity>>
} 