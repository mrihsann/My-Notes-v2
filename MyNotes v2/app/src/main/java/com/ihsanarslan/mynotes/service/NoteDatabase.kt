package com.ihsanarslan.mynotes.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ihsanarslan.mynotes.model.NoteDB
import com.ihsanarslan.mynotes.model.TrashDB

@Database(entities = [NoteDB::class, TrashDB::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun trashDao(): TrashDao

    companion object {
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                synchronized(NoteDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java, "my_note"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}