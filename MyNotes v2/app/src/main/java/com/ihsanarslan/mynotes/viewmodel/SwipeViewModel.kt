package com.ihsanarslan.mynotes.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.adapter.firebaseNotesAdapter
import com.ihsanarslan.mynotes.model.Note
import com.ihsanarslan.mynotes.model.NoteDB
import com.ihsanarslan.mynotes.model.NoteFirestore
import com.ihsanarslan.mynotes.model.TrashDB
import com.ihsanarslan.mynotes.noteDatabase
import com.ihsanarslan.mynotes.util.auth
import com.ihsanarslan.mynotes.util.notesCollectionRef
import com.ihsanarslan.mynotes.firebaseNotes
import kotlinx.coroutines.launch

class SwipeViewModel(application: Application) :BaseViewModel(application) {

    fun delNote(currentitem:Note){
        launch {
            val newnote = NoteDB(
                currentitem.id,
                currentitem.title,
                currentitem.content,
                currentitem.color,
                currentitem.liked
            )
            val trashnote = TrashDB(
                currentitem.id,
                currentitem.title,
                currentitem.content,
                currentitem.color,
                currentitem.liked
            )
            noteDatabase.noteDao().delete(newnote)
            noteDatabase.trashDao().insert(trashnote)
        }
    }

    fun saveNote(currentitem:Note){
        launch {
            val newnote = NoteDB(
                currentitem.id,
                currentitem.title,
                currentitem.content,
                currentitem.color,
                currentitem.liked
            )
            val trashnote = TrashDB(
                currentitem.id,
                currentitem.title,
                currentitem.content,
                currentitem.color,
                currentitem.liked
            )
            noteDatabase.noteDao().insert(newnote)
            noteDatabase.trashDao().delete(trashnote)
        }
    }

    fun delTrashNote(currentitem: Note){
        launch {
            val trashnote = TrashDB(
                currentitem.id,
                currentitem.title,
                currentitem.content,
                currentitem.color,
                currentitem.liked
            )
            noteDatabase.trashDao().delete(trashnote)
        }
    }

    fun downloadNote(currentitem: NoteFirestore,noteDB: NoteDB){
        // notların koleksiyonundan başlık ve içerik özelliklerinin her ikisi de yeni notla eşleşenleri sorgula
        launch {
            val NoteQuery = noteDatabase.noteDao().getNoteById(currentitem.title,currentitem.content,currentitem.color.toInt(),currentitem.liked)
            if (NoteQuery.isEmpty()){
                noteDatabase.noteDao().insert(noteDB)
            }
            firebaseNotesAdapter(firebaseNotes).notifyDataSetChanged()
        }

    }


    //kullanıcının seçtiği notu siler
    fun deleteNoteByUserCloud(title: String, color: Long, liked: Boolean,viewHolder: RecyclerView.ViewHolder) {
        val query = notesCollectionRef
            .whereEqualTo("user", auth.currentUser?.email.toString())
            .whereEqualTo("title", title)
            .whereEqualTo("color", color)
            .whereEqualTo("liked", liked)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    notesCollectionRef.document(document.id).delete()
                        .addOnSuccessListener {
                            // silme işlemi için kullanıcıya bir mesaj gösterme
                            Snackbar.make(
                                viewHolder.itemView,
                                R.string.delete_note_cloud,
                                Snackbar.LENGTH_LONG
                            ).setTextColor(Color.RED).show()
                        }
                        .addOnFailureListener { e ->
                            Snackbar.make(
                                viewHolder.itemView,
                                R.string.failed_delete_note_cloud,
                                Snackbar.LENGTH_LONG
                            ).setTextColor(Color.RED).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Snackbar.make(
                    viewHolder.itemView,
                    R.string.unknown_delete_error_note,
                    Snackbar.LENGTH_LONG
                ).setTextColor(Color.RED).show()
            }
    }

}
