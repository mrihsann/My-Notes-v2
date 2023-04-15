package com.ihsanarslan.mynotes.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ihsanarslan.mynotes.firebaseNotes
import com.ihsanarslan.mynotes.model.NoteDB
import com.ihsanarslan.mynotes.model.NoteFirestore
import com.ihsanarslan.mynotes.service.NoteDao
import com.ihsanarslan.mynotes.service.NoteDatabase
import com.ihsanarslan.mynotes.util.auth
import com.ihsanarslan.mynotes.util.db
import com.ihsanarslan.mynotes.util.notesCollectionRef
import kotlinx.coroutines.launch

class CloudViewModel(application: Application) : BaseViewModel(application) {
    var notesList= MutableLiveData<List<NoteFirestore>>()
    var notesEmpty= MutableLiveData<Boolean>()


    //kullanıcının tüm notlarını firebaseden çeker
    fun getAllUserNote() {
        launch {
            notesCollectionRef.whereEqualTo("user", auth.currentUser?.email.toString())
                .addSnapshotListener { snapshot,error ->
                    val notes = mutableListOf<NoteFirestore>()
                    for (document in snapshot?.documents!!) {
                        val title = document.get("title") as String
                        val content = document.get("content") as String
                        val color = document.get("color") as Long
                        val liked = document.get("liked") as Boolean
                        val user = document.get("user") as String
                        val note = NoteFirestore(title, content, color, liked, user)
                        notes.add(note)
                    }
                    notesEmpty.value = snapshot.isEmpty
                    notesList.value = notes
                }
        }

    }

    //çalıştığı zaman telefonun veritabanında ki tüm notları firestoreye kaydeder.
    fun addAllNoteFirestore(context: Context){
        launch {
            val noteDatabase = NoteDatabase.getInstance(context)
            // NoteDao nesnesini oluşturun
            val noteDao = noteDatabase.noteDao()
            // Tüm notları veritabannından çekip noteLİst'e yazıyoruz
            val notes = noteDao.getAllNotes()
            //room veritabanında ki notları çekip listemize ekliyoruz.
            notes.forEach { note ->
                val noteMap=HashMap<String,Any>()
                noteMap["user"] = auth.currentUser?.email.toString()
                noteMap["title"] = note.title
                noteMap["content"] = note.content
                noteMap["color"] = note.color
                noteMap["liked"] = note.liked

                //not eklenir
                db.collection("Notes")
                    .add(noteMap)
                    .addOnSuccessListener { documentReference ->
                        println("not firebase veritabanına eklendi")
                    }
                    .addOnFailureListener { e ->
                        println("not firebase veritabanına eklenemedi")
                    }
            }
        }
    }

    //firebase üzerinde ki kulanıcıya ait tüm notları telefon hafızasına kaydeder
    fun downloadAllNote(noteDao:NoteDao){
        launch {
            firebaseNotes.forEach {
                val title=it.title
                val content=it.content
                val liked=it.liked
                val color=it.color
                val NoteQuery = noteDao.getNoteById(title,content,color.toInt(),liked)
                if (NoteQuery.isEmpty()){
                    noteDao.insert(NoteDB(0,title,content,color.toInt(),liked))
                }
            }
        }
    }

    fun deleteAllCloude(){
        launch {
            val query = notesCollectionRef.whereEqualTo("user", auth.currentUser?.email.toString()) // "deger" alan adı ve "silinmesi gereken deger" değeri

            query.get().addOnSuccessListener { querySnapshot ->
                val batch = db.batch()
                for (document in querySnapshot) {
                    batch.delete(document.reference)
                }
                batch.commit().addOnSuccessListener {
                    println("Tüm notlar silindi.")
                }
            }
        }
    }
}