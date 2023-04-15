package com.ihsanarslan.mynotes.viewmodel

import android.app.Application
import android.graphics.Color
import com.ihsanarslan.mynotes.model.NoteDB
import com.ihsanarslan.mynotes.service.NoteDao
import kotlinx.coroutines.launch

class AddViewModel(application: Application) :BaseViewModel(application) {
    fun addNote(titlee:String,contentt:String,colorr:String,noteDao:NoteDao){
        launch {
            val newnotee= NoteDB(0,titlee,contentt, Color.parseColor(colorr),false)
            noteDao.insert(newnotee)
        }
    }

}