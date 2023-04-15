package com.ihsanarslan.mynotes.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ihsanarslan.mynotes.adapter.TrashAdapter
import com.ihsanarslan.mynotes.model.Note
import com.ihsanarslan.mynotes.noteTrashList
import com.ihsanarslan.mynotes.service.TrashDao
import kotlinx.coroutines.launch

class TrashViewModel(application: Application) :BaseViewModel(application) {
    var notesList= MutableLiveData<List<Note>>()
    var notesEmpty= MutableLiveData<Boolean>()

    fun getNote(trashDao:TrashDao,trashAdapter: TrashAdapter){
        //önce tüm notları siliyoruz
        noteTrashList.clear()
        // Tüm notları veritabannından çekip noteLİst'e yazıyoruz
        launch {
            val trashnotes = trashDao.getAllNotes()
            trashnotes.forEach { note ->
                val trashnote= Note(note.id,note.title,note.content,note.color,note.liked)
                if (trashnote !in noteTrashList){
                    noteTrashList.add(trashnote)
                }
            }
            notesEmpty.value = trashnotes.isEmpty()

            trashAdapter.notifyDataSetChanged()
        }
        notesList.value= noteTrashList
    }
}