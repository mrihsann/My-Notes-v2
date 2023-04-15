package com.ihsanarslan.mynotes.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ihsanarslan.mynotes.adapter.NoteAdapter
import com.ihsanarslan.mynotes.model.Note
import com.ihsanarslan.mynotes.model.NoteDB
import com.ihsanarslan.mynotes.noteList
import com.ihsanarslan.mynotes.service.NoteDao
import kotlinx.coroutines.launch

class ListViewModel(application: Application) :BaseViewModel(application) {
    var notesList=MutableLiveData<List<Note>>()
    var notesEmpty=MutableLiveData<Boolean>()

    fun getNote(noteDao: NoteDao,noteAdapter:NoteAdapter){
        //önce tüm notları siliyoruz
        noteList.clear()
        // Tüm notları veritabannından çekip noteLİst'e yazıyoruz
        launch {
            val notes = noteDao.getAllNotes()
            //room veritabanında ki notları çekip listemize ekliyoruz.
            notes.forEach { note ->
                val newnote = Note(note.id, note.title, note.content, note.color, note.liked)
                if (newnote !in noteList) {
                    noteList.add(newnote)
                }
            }
            notesEmpty.value = notes.isEmpty()
            noteAdapter.notifyDataSetChanged()
        }

        notesList.value= noteList
    }
    fun updateLiked(currentItem:Note,noteDao: NoteDao,liked:Boolean){
        launch {
            val title = currentItem.title
            val content = currentItem.content
            val color= currentItem.color
            noteDao.update(NoteDB(currentItem.id,title,content,color,liked))
        }
    }
}