package com.ihsanarslan.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ihsanarslan.mynotes.model.Note
import com.ihsanarslan.mynotes.service.NoteDatabase
import com.ihsanarslan.mynotes.databinding.ActivityMainBinding
import com.ihsanarslan.mynotes.model.NoteFirestore
import com.ihsanarslan.mynotes.util.barcolor
lateinit var noteTrashList: ArrayList<Note>
lateinit var noteList: ArrayList<Note>
lateinit var firebaseNotes:ArrayList<NoteFirestore>
lateinit var noteDatabase: NoteDatabase
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //telefonun back tuşunu etkisiz kılıyoruz
    override fun onBackPressed() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.auth
        //database oluşturuyoruz
        noteDatabase = NoteDatabase.getInstance(this)


        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        noteList= ArrayList()
        noteTrashList =ArrayList()
        firebaseNotes =ArrayList()


        barcolor(R.color.background,this,this)

    }

}