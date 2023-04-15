package com.ihsanarslan.mynotes.viewmodel

import android.app.Application
import com.ihsanarslan.mynotes.model.NoteDB
import com.ihsanarslan.mynotes.model.TrashDB
import com.ihsanarslan.mynotes.service.NoteDao
import com.ihsanarslan.mynotes.service.TrashDao
import com.ihsanarslan.mynotes.util.auth
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) :BaseViewModel(application) {
    fun notToTrashTransport(noteDao:NoteDao,trashDao:TrashDao){
        launch {
            val NoteList = noteDao.getAllNotes()
            for (note in NoteList) {
                val notee = TrashDB(
                    0,
                    note.title,
                    note.content,
                    note.color,
                    note.liked
                )
                trashDao.insert(notee)
            }
            noteDao.deleteAllNotes()
        }
    }

    fun deleteTrashNotes(trashDao: TrashDao){
        launch {
            trashDao.deleteAllTrashNotes()
        }
    }
    fun signOut(){
        auth.signOut()
    }
    fun recoverTrashNotes(trashDao: TrashDao,noteDao: NoteDao) {
        launch {
            val trashNoteList = trashDao.getAllNotes()
            for (note in trashNoteList) {
                val noteintrash = NoteDB(
                    0,
                    note.title,
                    note.content,
                    note.color,
                    note.liked
                )
                noteDao.insert(noteintrash)
            }
            trashDao.deleteAllTrashNotes()
        }

    }
}