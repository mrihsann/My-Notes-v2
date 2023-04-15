package com.ihsanarslan.mynotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.databinding.FragmentListBinding
import com.ihsanarslan.mynotes.model.NoteDB
import com.ihsanarslan.mynotes.service.NoteDao
import com.ihsanarslan.mynotes.view.ListFragmentDirections
import com.ihsanarslan.mynotes.model.Note
import com.ihsanarslan.mynotes.viewmodel.ListViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class NoteAdapter(private val notes: ArrayList<Note>, val noteDao: NoteDao,val owner: ViewModelStoreOwner) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //itemviewin içeriğinde ki bilgileri buluyoruz
        private val titleTextView: TextView = itemView.findViewById(R.id.itemtitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.itemcontent)
        private val noteCardd:CardView=itemView.findViewById(R.id.cardView)

        fun bind(note: Note) {
            if(note.title.length>30){
                titleTextView.text = note.title.substring(0,30)+"..."
            }
            else{
                titleTextView.text = note.title
            }
            if(note.content.length>50){
                descriptionTextView.text = note.content.substring(0,50)+"..."
            }
            else{
                descriptionTextView.text = note.content
            }
            noteCardd.setCardBackgroundColor(note.color)
            if (note.liked){
                noteCardd.findViewById<ImageView>(R.id.favButoon).setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            else{
                noteCardd.findViewById<ImageView>(R.id.favButoon).setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }

        }
    }

    //filtrelediğimiz verileri geçici bir listede tutuyoruz.
    private var filteredList: List<Note> = notes
    //filtreleme fonksiyonunu yazıyoruz


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            val action= ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem.title,currentItem.content,currentItem.color,currentItem.id,currentItem.liked)
            Navigation.findNavController(it).navigate(action)
        }

        holder.itemView.findViewById<ImageView>(R.id.favButoon).setOnClickListener {
            //view modelimizi oluşturuyoruz.
            val viewModel = ViewModelProvider(owner)[ListViewModel::class.java]
            if (currentItem.liked){
                //veritabanından 0 yap liked değerini
                currentItem.liked=false
                viewModel.updateLiked(currentItem,noteDao,false)
                holder.itemView.findViewById<ImageView>(R.id.favButoon).setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            else{
                //veritabanından 1 yap liked değerini
                currentItem.liked=true
                viewModel.updateLiked(currentItem,noteDao,true)
                holder.itemView.findViewById<ImageView>(R.id.favButoon).setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            notifyItemChanged(position)
        }
    }


    override fun getItemCount(): Int {
        return filteredList.size
    }
    //arama algoritmasında uygulanan filtreleme işlemi
    fun filter(query: String,binding: FragmentListBinding) {
        filteredList = notes.filter {
            it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
        }
        if (filteredList.isEmpty()){
            binding.emtySearch.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.item0.visibility = View.GONE
        }else{
            binding.emtySearch.visibility = View.GONE
            binding.item0.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
        notifyDataSetChanged()
    }

    //favoriler sayfasında uygulanan filtreleme işlemi
    fun filterLiked(binding: FragmentListBinding) {
        filteredList = notes.filter {
            it.liked==true
        }
        if (filteredList.isEmpty()){
            binding.emtyLiked.visibility=View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.item0.visibility = View.GONE
        }else{
            binding.emtyLiked.visibility=View.GONE
            binding.item0.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
        notifyDataSetChanged()
    }

    //viewmodel için yazılan güncelleme
    fun updateNoteList(newNoteList:List<Note>){
        notes.clear()
        notes.addAll(newNoteList)
        notifyDataSetChanged()
    }
}