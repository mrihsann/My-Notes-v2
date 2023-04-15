package com.ihsanarslan.mynotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.databinding.FragmentTrashBinding
import com.ihsanarslan.mynotes.model.Note
import kotlin.collections.ArrayList


class TrashAdapter(private val notes: ArrayList<Note>) : RecyclerView.Adapter<TrashAdapter.TrashViewHolder>() {
    private var filteredList: List<Note> = notes
    class TrashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //itemviewin içeriğinde ki bilgileri buluyoruz
        private val titleTextView: TextView = itemView.findViewById(R.id.itemtitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.itemcontent)
        private val noteCardd:CardView=itemView.findViewById(R.id.cardView)

        fun bind(note: Note) {
            if(note.title.length>40){
                titleTextView.text = note.title.substring(0,40)+"..."
            }
            else{
                titleTextView.text = note.title
            }
            if(note.content.length>80){
                descriptionTextView.text = note.content.substring(0,80)+"..."
            }
            else{
                descriptionTextView.text = note.content
            }
            if (note.liked){
                noteCardd.findViewById<ImageView>(R.id.favButoonTrash).setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            else{
                noteCardd.findViewById<ImageView>(R.id.favButoonTrash).setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            noteCardd.setCardBackgroundColor(note.color)
        }
    }

    //arama algoritmasını filtreleme fonksiyonunu yazıyoruz
    fun filter(query: String,binding:FragmentTrashBinding) {
        filteredList = notes.filter {
            it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
        }
        if (filteredList.isEmpty()){
            binding.emtySearch.visibility = View.VISIBLE
            binding.recyclerViewTrash.visibility = View.GONE
            binding.item0.visibility = View.GONE
        }else{
            binding.emtySearch.visibility = View.GONE
            binding.item0.visibility = View.GONE
            binding.recyclerViewTrash.visibility = View.VISIBLE
        }
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trash_item, parent, false)
        return TrashViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrashViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    //viewmodel için güncelleme
    fun updateNoteList(newNoteList:List<Note>){
        notes.clear()
        notes.addAll(newNoteList)
        notifyDataSetChanged()
    }
}