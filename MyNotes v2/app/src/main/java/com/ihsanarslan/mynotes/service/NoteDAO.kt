package com.ihsanarslan.mynotes.service

import androidx.room.*
import com.ihsanarslan.mynotes.model.NoteDB

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: NoteDB)

    @Update
    suspend fun update(note: NoteDB)

    @Delete
    suspend fun delete(note: NoteDB)

    @Query("SELECT * FROM Notes ORDER BY id DESC")
    suspend fun getAllNotes(): List<NoteDB>

    @Query("SELECT * FROM Notes WHERE noteTitle = :title AND noteContent = :content AND noteColor = :color AND likedBoolen = :liked")
    suspend fun getNoteById(title: String, content:String, color: Int, liked: Boolean): List<NoteDB>

    @Query("DELETE FROM Notes")
    suspend fun deleteAllNotes()
}