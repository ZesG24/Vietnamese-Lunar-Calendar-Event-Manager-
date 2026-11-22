package com.example.amlich.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GioDao {
    @Query("SELECT * FROM gio_items ORDER BY lunarMonth ASC, lunarDay ASC")
    fun getAllGioItems(): Flow<List<GioItemEntity>>

    @Query("SELECT * FROM gio_items WHERE id = :id")
    suspend fun getGioItemById(id: Long): GioItemEntity?

    @Query("SELECT * FROM gio_items WHERE lunarMonth = :month ORDER BY lunarDay ASC")
    fun getGioItemsByMonth(month: Int): Flow<List<GioItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGioItem(item: GioItemEntity): Long

    @Update
    suspend fun updateGioItem(item: GioItemEntity)

    @Delete
    suspend fun deleteGioItem(item: GioItemEntity)

    @Query("DELETE FROM gio_items WHERE id = :id")
    suspend fun deleteGioItemById(id: Long)
}
