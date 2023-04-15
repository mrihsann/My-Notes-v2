package com.ihsanarslan.mynotes.view

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.ihsanarslan.mynotes.*
import com.ihsanarslan.mynotes.adapter.TrashAdapter
import com.ihsanarslan.mynotes.service.NoteDao
import com.ihsanarslan.mynotes.service.TrashDao
import com.ihsanarslan.mynotes.databinding.FragmentTrashBinding
import com.ihsanarslan.mynotes.service.NoteDatabase
import com.ihsanarslan.mynotes.swipe.SwipeToUndoCallback
import com.ihsanarslan.mynotes.util.auth
import com.ihsanarslan.mynotes.viewmodel.TrashViewModel

//çöp kutusundayken favoriler menüsene tıkladı mı diye kontrol değişkeni tutuyoruz. Nu değere göre listFragmentte aksiyonlar alacağız.
var favFollow=0
class TrashFragment : Fragment() {
    private lateinit var viewModel: TrashViewModel
    private lateinit var binding: FragmentTrashBinding
    private lateinit var noteDao: NoteDao
    private lateinit var trashDao: TrashDao
    private lateinit var trashAdapter: TrashAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(requireContext()) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // NoteDao nesnesini oluşturun
        // NoteDatabase nesnesini ve DAO'ları oluşturuyoruz
        val noteDatabase = NoteDatabase.getInstance(requireContext())
        trashDao = noteDatabase.trashDao()
        noteDao = noteDatabase.noteDao()

        // RecyclerView'ı oluştur
        recyclerView = binding.recyclerViewTrash
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //recylerview içinde bulunan son notun altına boşluk bırakıyoruz ki alt menü ile çakışmasın
        val itemDecoration = BottomSpaceItemDecoration(170)
        recyclerView.addItemDecoration(itemDecoration)

        // Adapter'ı oluştur ve RecyclerView'a bağla
        trashAdapter = TrashAdapter(noteTrashList)
        recyclerView.adapter = trashAdapter

        //view modelimizi oluşturuyoruz.
        viewModel = ViewModelProvider(this)[TrashViewModel::class.java]
        viewModel.getNote(trashDao,trashAdapter)

        observeLiveData()

        //swiftrefresh
        binding.swiftRefresh.setOnRefreshListener {
            viewModel.getNote(trashDao,trashAdapter)
            binding.swiftRefresh.isRefreshing=false
        }

        //fragment açıldığı zaman trash menüsünün tıklanmış şekilde gösterilmesini sağlıyoruz.
        binding.bottomNavigationTrash.selectedItemId=R.id.trash
        //menu ayarları
        binding.bottomNavigationTrash.background=null
        binding.bottomNavigationTrash.menu.getItem(2).isEnabled=false
        //reklam gösterimi
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // SearchView işlemleri burada yapılabilir
        val searchView = binding.searchViewTrash
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                trashAdapter.filter(newText ?: "",binding)
                return true
            }
        })

        //reklam kısmını açıp kapatma olayı
        binding.adsbutton.setOnClickListener {
            binding.adsbutton.alpha = 0.5f // Geçici olarak alfa değerini azaltın
            Handler().postDelayed({
                binding.adsbutton.alpha = 1.0f // Alfa değerini geri yükleyin
            }, 200) // 200 milisaniye bekletin

            if (binding.adView.visibility == VISIBLE) {
                binding.adView.visibility=GONE
            } else {
                binding.adView.visibility= VISIBLE
            }
        }

        //arama çubuğunu açıp kapatma olayı
        binding.search.setOnClickListener {
            binding.search.alpha = 0.5f // Geçici olarak alfa değerini azaltın
            Handler().postDelayed({
                binding.search.alpha = 1.0f // Alfa değerini geri yükleyin
            }, 200) // 200 milisaniye bekletin

            if (searchView.visibility == VISIBLE) {
                searchView.visibility=GONE
            } else {
                searchView.visibility= VISIBLE
            }
        }

        //not ekleme kısmı
        binding.addbuttontrash.setOnClickListener {
            val action = TrashFragmentDirections.actionTrashFragmentToAddFragment()
            Navigation.findNavController(view).navigate(action)
        }

        //menü kısmı
        binding.bottomNavigationTrash.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    favFollow=0
                    val action=TrashFragmentDirections.actionTrashFragmentToListFragment()
                    Navigation.findNavController(view).navigate(action)
                }
                R.id.favButton -> {
                    favFollow=1
                    val action=TrashFragmentDirections.actionTrashFragmentToListFragment()
                    Navigation.findNavController(view).navigate(action)
                }
                R.id.trash -> {
                    favFollow=0
                    val action = TrashFragmentDirections.actionTrashFragmentSelf()
                    Navigation.findNavController(view).navigate(action)
                }
                R.id.settings -> {
                    favFollow=0
                    val currentUser=auth.currentUser
                    if (currentUser!=null) {
                        val action=TrashFragmentDirections.actionTrashFragmentToSettingsFragment()
                        Navigation.findNavController(view).navigate(action)
                    }else{
                        val action = TrashFragmentDirections.actionTrashFragmentToSettingsLoginFragment()
                        Navigation.findNavController(view).navigate(action)
                    }

                }
            }
            true
        }


        //itemi sağa yada sola kaydırarak geri kurtarıyoruz
        val itemTouchHelper = ItemTouchHelper(SwipeToUndoCallback(view,trashAdapter,requireContext(),this))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun observeLiveData(){
        viewModel.notesList.observe(viewLifecycleOwner) { notes ->
            notes?.let {
                trashAdapter.updateNoteList(notes)
            }

        }

        viewModel.notesEmpty.observe(viewLifecycleOwner){
            if (it){
                binding.item0.visibility = View.VISIBLE
                binding.recyclerViewTrash.visibility = View.GONE
            }else{
                binding.item0.visibility = View.GONE
                binding.recyclerViewTrash.visibility = View.VISIBLE
            }
        }

    }
}