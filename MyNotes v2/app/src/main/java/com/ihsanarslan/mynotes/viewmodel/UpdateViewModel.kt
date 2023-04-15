package com.ihsanarslan.mynotes.viewmodel

import android.app.Application
import android.graphics.Color
import com.ihsanarslan.mynotes.databinding.FragmentUpdateBinding
import com.ihsanarslan.mynotes.model.NoteDB
import com.ihsanarslan.mynotes.service.NoteDao
import kotlinx.coroutines.launch

class UpdateViewModel(application: Application) :BaseViewModel(application) {
    fun updateNote(binding: FragmentUpdateBinding,noteDao:NoteDao,colorr:Int,id:Int,liked:Boolean){
        launch {
            val title = binding.addTitleU.text.toString()
            val content = binding.addContentU.text.toString()
            noteDao.update(NoteDB(id, title, content, colorr, liked))
        }
    }
}