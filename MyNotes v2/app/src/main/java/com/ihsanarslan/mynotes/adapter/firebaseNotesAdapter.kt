package com.ihsanarslan.mynotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.databinding.FirebaseRecyclerRowBinding
import com.ihsanarslan.mynotes.view.CloudFragmentDirections
import com.ihsanarslan.mynotes.model.NoteFirestore

class firebaseNotesAdapter(private val firebaseNotes:ArrayList<NoteFirestore>):RecyclerView.Adapter<firebaseNotesAdapter.FirebaseNoteHolder>() {
    class FirebaseNoteHolder(val binding: FirebaseRecyclerRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseNoteHolder {
        val binding=FirebaseRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FirebaseNoteHolder(binding)
    }

    override fun getItemCount(): Int {
        return firebaseNotes.size
    }

    override fun onBindViewHolder(holder: FirebaseNoteHolder, position: Int) {
        val note= firebaseNotes[position]
        holder.binding.cardView.setCardBackgroundColor(firebaseNotes[position].color.toInt())
        holder.itemView.setOnClickListener {
            val action= CloudFragmentDirections.actionCloudFragmentToReadCloudNoteFragment(note.title,note.content,note.color.toInt())
            Navigation.findNavController(it).navigate(action)
        }
        if(note.title.length>30){
            holder.binding.itemtitleDB.text= firebaseNotes[position].title.substring(0,30)+"..."
        }
        else{
            holder.binding.itemtitleDB.text= firebaseNotes[position].title
        }
        if(note.content.length>50){
            holder.binding.itemcontentDB.text= firebaseNotes[position].content.substring(0,50)+"..."
        }
        else{
            holder.binding.itemcontentDB.text= firebaseNotes[position].content
        }
        if (note.liked) {
            holder.binding.favButoonDB.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            holder.binding.favButoonDB.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    fun updateNoteList(newNoteList:List<NoteFirestore>){
        firebaseNotes.clear()
        firebaseNotes.addAll(newNoteList)
        notifyDataSetChanged()
    }
}
