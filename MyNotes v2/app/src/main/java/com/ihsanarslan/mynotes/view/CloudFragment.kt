package com.ihsanarslan.mynotes.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ihsanarslan.mynotes.BottomSpaceItemDecoration
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.adapter.firebaseNotesAdapter
import com.ihsanarslan.mynotes.service.NoteDao
import com.ihsanarslan.mynotes.databinding.FragmentCloudBinding
import com.ihsanarslan.mynotes.firebaseNotes
import com.ihsanarslan.mynotes.service.NoteDatabase
import com.ihsanarslan.mynotes.swipe.SwipeToCloudCallback
import com.ihsanarslan.mynotes.viewmodel.CloudViewModel



class CloudFragment : Fragment() {
    private lateinit var binding: FragmentCloudBinding
    private lateinit var noteDao: NoteDao
    private lateinit var dbNoteAdapter: firebaseNotesAdapter
    private lateinit var viewModel: CloudViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCloudBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // NoteDatabase nesnesini ve DAO'ları oluşturuyoruz
        val noteDatabase = NoteDatabase.getInstance(requireContext())
        noteDao = noteDatabase.noteDao()

        binding.firebaseNotesRecyclerView.layoutManager=LinearLayoutManager(requireContext())

        //recylerview içinde bulunan son notun altına boşluk bırakıyoruz ki alt menü ile çakışmasın
        val itemDecoration = BottomSpaceItemDecoration(170)
        binding.firebaseNotesRecyclerView.addItemDecoration(itemDecoration)

        dbNoteAdapter=firebaseNotesAdapter(firebaseNotes)
        binding.firebaseNotesRecyclerView.adapter=dbNoteAdapter

        viewModel = ViewModelProvider(this)[CloudViewModel::class.java]
        viewModel.getAllUserNote()

        observeLiveData()

        BottomSheetBehavior.from(binding.bottomSheet).apply{
            peekHeight=100
            this.state=BottomSheetBehavior.STATE_COLLAPSED
        }

        //swiftrefresh
        binding.swiftRefresh.setOnRefreshListener {
            viewModel.getAllUserNote()
            binding.swiftRefresh.isRefreshing=false
        }

        //butonların işlevi
        binding.backCloud.setOnClickListener{
            val action=CloudFragmentDirections.actionCloudFragmentToSettingsFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.upload.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.warning)
            builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
            builder.setMessage(R.string.cloud_saved_notes)
                .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                .setPositiveButton(R.string.yes) { dialog, id ->
                    viewModel.addAllNoteFirestore(requireContext())
                }
                .setNegativeButton(R.string.no) { dialog, id ->
                    // Hayır'a tıklandığında yapılacak işlemler buraya yazılır
                    dialog.cancel() // Uyarı mesajını kapatır
                }
            val alert = builder.create()
            alert.show()
            // Evet düğmesinin yazı rengini yeşil olarak ayarla
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#2C9430"))

            // Hayır düğmesinin yazı rengini kırmızı olarak ayarla
            alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.RED)
        }
        binding.download.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.warning)
            builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
            builder.setMessage(R.string.cloud_download_notes)
                .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                .setPositiveButton(R.string.yes) { dialog, id ->
                    viewModel.downloadAllNote(noteDao)
                }
                .setNegativeButton(R.string.no) { dialog, id ->
                    dialog.cancel()
                }
            val alert = builder.create()
            alert.show()
            // Evet düğmesinin yazı rengini yeşil olarak ayarla
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#2C9430"))

            // Hayır düğmesinin yazı rengini kırmızı olarak ayarla
            alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.RED)
        }
        binding.refresh.setOnClickListener{
            val action=CloudFragmentDirections.actionCloudFragmentSelf()
            Navigation.findNavController(view).navigate(action)
        }
        binding.deleteAllCloud.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.warning)
            builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
            builder.setMessage(R.string.cloud_delete_all_notes)
                .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                .setPositiveButton(R.string.yes) { dialog, id ->
                    viewModel.deleteAllCloude()
                }
                .setNegativeButton(R.string.no) { dialog, id ->
                    dialog.cancel()
                }
            val alert = builder.create()
            alert.show()
            // Evet düğmesinin yazı rengini kırmızı olarak ayarla
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED)

            // Hayır düğmesinin yazı rengini yeşil olarak ayarla
            alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#2C9430"))

        }

        //listede ki itemlerin sağa kaydırma işlemini burada aktif ediyoruz.
        val itemTouchHelper = ItemTouchHelper(
            SwipeToCloudCallback(view, requireContext(),firebaseNotesAdapter(firebaseNotes),this)
        )
        itemTouchHelper.attachToRecyclerView(binding.firebaseNotesRecyclerView)
    }

    private fun observeLiveData(){
        viewModel.notesList.observe(viewLifecycleOwner) { notes ->
            notes?.let {
                dbNoteAdapter.updateNoteList(notes)
            }
        }
        viewModel.notesEmpty.observe(viewLifecycleOwner){
            if (it){
                binding.itemEmpty.visibility = View.VISIBLE
                binding.firebaseNotesRecyclerView.visibility = View.GONE
            }else{
                binding.itemEmpty.visibility = View.GONE
                binding.firebaseNotesRecyclerView.visibility = View.VISIBLE
            }
        }
    }

}