package com.ihsanarslan.mynotes.service

import androidx.room.*
import com.ihsanarslan.mynotes.model.TrashDB

@Dao
interface TrashDao {
    @Insert
    suspend fun insert(note: TrashDB)

    @Update
    suspend fun update(note: TrashDB)

    @Delete
    suspend fun delete(note: TrashDB)

    @Query("SELECT * FROM TrashNotes ORDER BY id DESC")
    suspend fun getAllNotes(): List<TrashDB>

    @Query("DELETE FROM TrashNotes")
    suspend fun deleteAllTrashNotes()
}