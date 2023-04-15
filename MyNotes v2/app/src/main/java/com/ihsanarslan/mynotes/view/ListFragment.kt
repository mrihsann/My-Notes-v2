package com.ihsanarslan.mynotes.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.ihsanarslan.mynotes.BottomSpaceItemDecoration
import com.ihsanarslan.mynotes.adapter.NoteAdapter
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.swipe.SwipeToDeleteCallback
import com.ihsanarslan.mynotes.service.*
import com.ihsanarslan.mynotes.databinding.FragmentListBinding
import com.ihsanarslan.mynotes.noteList
import com.ihsanarslan.mynotes.service.NoteDao
import com.ihsanarslan.mynotes.service.NoteDatabase
import com.ihsanarslan.mynotes.service.TrashDao
import com.ihsanarslan.mynotes.util.auth
import com.ihsanarslan.mynotes.util.barcolor
import com.ihsanarslan.mynotes.viewmodel.ListViewModel

class ListFragment : Fragment() {
    private lateinit var viewModel: ListViewModel
    private lateinit var binding: FragmentListBinding
    private lateinit var noteDao: NoteDao
    private lateinit var trashDao: TrashDao
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //reklamı bağlıyoruz
        MobileAds.initialize(requireContext()) {}
        //fragment ilk açılıdğında bar rengini değiştiriyoruz ki arka plan rengiyle aynı olsun
        barcolor(R.color.background,requireActivity(),requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // NoteDatabase nesnesini ve DAO'ları oluşturuyoruz
        val noteDatabase = NoteDatabase.getInstance(requireContext())
        noteDao = noteDatabase.noteDao()
        trashDao = noteDatabase.trashDao()

        // RecyclerView'ı oluşturuyoruz
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //recylerview içinde bulunan son notun altına boşluk bırakıyoruz ki alt menünün arkasında durmasın son not
        val itemDecoration = BottomSpaceItemDecoration(170)
        recyclerView.addItemDecoration(itemDecoration)

        // Adapter'ı oluşturup ve RecyclerView'a bağlıyoruz
        noteAdapter = NoteAdapter(noteList, noteDao,this)
        recyclerView.adapter = noteAdapter

        //view modelimizi oluşturuyoruz.
        viewModel = ViewModelProvider(this)[ListViewModel::class.java]

        //uygulama ilk açıldığında, notları çağırıyoruz
        viewModel.getNote(noteDao,noteAdapter)

        //notları gözlemliyoruz
        observeLiveData()

        //swiftrefresh
        binding.swiftRefresh.setOnRefreshListener {
            val action = ListFragmentDirections.actionListFragmentSelf()
            Navigation.findNavController(view).navigate(action)
            binding.swiftRefresh.isRefreshing=false
        }
        //çöp kutusundayken fav butonu tıklandığı zaman filtrleme işlemi yapıyoruz.
        if(favFollow==1){
            favFollow=0
            binding.bottomNavigation.selectedItemId=R.id.favButton
            noteAdapter.filterLiked(binding)
        }
        else{
            binding.bottomNavigation.selectedItemId=R.id.home
        }

        // SharedPreferences'tan veri okuma
        val sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString("userName", "Kanka")

        // SharedPreferences'tan okunan veriyi yazma
        binding.textView5.text = "$value!\uD83D\uDC4B"

        //menü ayarları
        binding.bottomNavigation.background = null
        binding.bottomNavigation.menu.getItem(2).isEnabled = false

        //reklam kodları
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // SearchView işlemleri
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                noteAdapter.filter(newText ?: "",binding)
                return true
            }
        })

        //reklam aç kapa butonu
        binding.adsbutton.setOnClickListener {
            binding.adsbutton.alpha = 0.5f // Geçici olarak alfa değerini azaltın
            Handler().postDelayed({
                binding.adsbutton.alpha = 1.0f // Alfa değerini geri yükleyin
            }, 200) // 200 milisaniye bekletin

            if (binding.adView.visibility== VISIBLE) {
                binding.adView.visibility= GONE
            } else {
                binding.adView.visibility= VISIBLE
            }
        }

        //arama çubuğunu aç kapa butonu
        binding.search.setOnClickListener {
            binding.search.alpha = 0.5f // Geçici olarak alfa değerini azaltın
            Handler().postDelayed({
                binding.search.alpha = 1.0f // Alfa değerini geri yükleyin
            }, 200) // 200 milisaniye bekletin
            if (searchView.visibility== VISIBLE){
                searchView.visibility=GONE
            }
            else{
                searchView.visibility= VISIBLE
            }
        }

        //menüde ki not ekle butonu
        binding.addbutton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddFragment()
            Navigation.findNavController(view).navigate(action)
        }

        //menüde ki itemlere tıklanınca yapılacak işlemler
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val action = ListFragmentDirections.actionListFragmentSelf()
                    Navigation.findNavController(view).navigate(action)

                }
                R.id.favButton -> {
                    noteAdapter.filterLiked(binding)

                }
                R.id.trash -> {
                    val action = ListFragmentDirections.actionListFragmentToTrashFragment()
                    Navigation.findNavController(view).navigate(action)

                }
                R.id.settings -> {
                    val currentUser = auth.currentUser

                    //eğer mevcut kullanıcı varsa oturumu açık olan settings sayfasına atıyoruz
                    if (currentUser != null) {
                        val action = ListFragmentDirections.actionListFragmentToSettingsFragment()
                        Navigation.findNavController(view).navigate(action)
                    } else { //eğer mevcut kullanıcı yoksa oturumu kapalı olan settings sayfasına atıyoruz
                        val action =
                            ListFragmentDirections.actionListFragmentToSettingsLoginFragment()
                        Navigation.findNavController(view).navigate(action)
                    }

                }
            }
            true
        }

        //listede ki itemlerin sağa sola kaydırma işlemini burada aktif ediyoruz.
        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback(
                view,
                noteAdapter,
                requireContext(),
                this
            )
        )
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun observeLiveData(){
        viewModel.notesList.observe(viewLifecycleOwner) { notes ->
            notes?.let {
                noteAdapter.updateNoteList(notes)
            }

        }
        viewModel.notesEmpty.observe(viewLifecycleOwner){
            if (it){
                binding.item0.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }else{
                binding.item0.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

}

