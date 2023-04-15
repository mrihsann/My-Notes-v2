package com.ihsanarslan.mynotes.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ihsanarslan.mynotes.adapter.NoteAdapter
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.databinding.FragmentUpdateBinding
import com.ihsanarslan.mynotes.noteDatabase
import com.ihsanarslan.mynotes.noteList
import com.ihsanarslan.mynotes.service.NoteDao
import com.ihsanarslan.mynotes.util.barcolor
import com.ihsanarslan.mynotes.util.resetImageViewSize
import com.ihsanarslan.mynotes.util.setImageViewSize
import com.ihsanarslan.mynotes.viewmodel.UpdateViewModel

class UpdateFragment : Fragment() {
    private lateinit var viewModel: UpdateViewModel
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var noteDao: NoteDao

    fun backAlert(view: View){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.not_saved)
        builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
        builder.setMessage(R.string.not_saved_content)
            .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
            .setPositiveButton(R.string.yes) { dialog, id ->
                // Evet'e tıklandığında yapılacak işlemler buraya yazılır
                val action= UpdateFragmentDirections.actionUpdateFragmentToListFragment()
                Navigation.findNavController(view).navigate(action)
            }
            .setNegativeButton(R.string.no) { dialog, id ->
                // Hayır'a tıklandığında yapılacak işlemler buraya yazılır
                dialog.cancel() // Uyarı mesajını kapatır
            }
        val alert = builder.create()
        alert.show()
        // Evet düğmesinin yazı rengini kırmızı olarak ayarla
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED)

        // Hayır düğmesinin yazı rengini yeşil olarak ayarla
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#2C9430"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        noteDao = noteDatabase.noteDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view modelimizi oluşturuyoruz.
        viewModel = ViewModelProvider(this)[UpdateViewModel::class.java]
        //renk butonlarına tıklanınca seçilen rengi tutuyoruz burada. Not eklendiği zaman bu rengi kullanacağız.
        var colorr="#FFF599"

        //eğer ki herhangi bir renk değişimi yapmadan güncelleme yaparsak rngi #26A69A şeklinde değiştiriyordu.
        //Bu sorunu düzeltmek için aşağıda ki when yapısı yazıldı.
        arguments?.let {
            when (UpdateFragmentArgs.fromBundle(it).color) {
                -24930 -> {
                    barcolor(R.color.color1,requireActivity(),requireContext())
                    colorr="#FF9E9E"}
                -2663 -> {
                    barcolor(R.color.color2,requireActivity(),requireContext())
                    colorr="#FFF599"}
                -7211889 -> {
                    barcolor(R.color.color3,requireActivity(),requireContext())
                    colorr="#91F48F"}
                -157185 -> {
                    barcolor(R.color.color4,requireActivity(),requireContext())
                    colorr="#FD99FF"}
                -6356993 -> {
                    barcolor(R.color.color5,requireActivity(),requireContext())
                    colorr="#9EFFFF"}
                -1 -> {
                    barcolor(R.color.color6,requireActivity(),requireContext())
                    colorr="#FFFFFF"}
                else -> {
                    barcolor(R.color.color2,requireActivity(),requireContext())
                    colorr="#FFF599" }
            }
        }
        //toolBar içinde ki back butona tıklandığı zaman yapılacak işlem
        binding.toolBarU.backButtonU.setOnClickListener {
            backAlert(it)
        }

        //update fragmanına gelen verileri kullandık burada
        arguments?.let {
            val id=UpdateFragmentArgs.fromBundle(it).id
            val title=UpdateFragmentArgs.fromBundle(it).title
            val content=UpdateFragmentArgs.fromBundle(it).content
            val color=UpdateFragmentArgs.fromBundle(it).color
            val liked=UpdateFragmentArgs.fromBundle(it).liked
            binding.addTitleU.setText(title)
            binding.addContentU.setText(content)
            binding.anaLayoutU.setBackgroundColor(color)

            //toolbar içinde ki update butonuna tıklandığı zaman güncelleme yaptırıyoruz
            binding.toolBarU.updatebuttonU.setOnClickListener {
                if (binding.addTitleU.length()!=0) {
                    viewModel.updateNote(binding,noteDao,Color.parseColor(colorr),id,liked)
                    NoteAdapter(noteList, noteDao,this).notifyItemChanged(id)
                    val action = UpdateFragmentDirections.actionUpdateFragmentToListFragment()
                    Navigation.findNavController(it).navigate(action)
                }
                else{
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(R.string.title_not_found)
                    builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
                    builder.setMessage(R.string.update_content)
                        .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                        .setPositiveButton(R.string.yes) { dialog, id ->
                            dialog.cancel()
                        }
                        .setNegativeButton(R.string.no) { dialog, id ->
                            val action= UpdateFragmentDirections.actionUpdateFragmentToListFragment()
                            Navigation.findNavController(view).navigate(action)
                        }
                    val alert = builder.create()
                    alert.show()
                    // Evet düğmesinin yazı rengini kırmızı olarak ayarla
                    alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#2C9430"))

                    // Hayır düğmesinin yazı rengini yeşil olarak ayarla
                    alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED)
                }
            }
            binding.toolBarU.colors.setOnClickListener {
                if (binding.colors.visibility == View.GONE) {
                    binding.colors.visibility = View.VISIBLE
                } else {
                    binding.colors.visibility = View.GONE
                }
            }
        }

        //renk butonlarına tıklandığı zaman arka plan rengini değiştiriyoruz
        binding.color1U.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color1))
            barcolor(R.color.color1,requireActivity(),requireContext())
            colorr="#FF9E9E"
            setImageViewSize(165,165, R.id.color1U,view)
            resetImageViewSize(R.id.color2U,view)
            resetImageViewSize(R.id.color3U,view)
            resetImageViewSize(R.id.color4U,view)
            resetImageViewSize(R.id.color5U,view)
            resetImageViewSize(R.id.color6U,view)

        }
        binding.color2U.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color2))
            barcolor(R.color.color2,requireActivity(),requireContext())
            colorr="#FFF599"
            setImageViewSize(165,165, R.id.color2U,view)
            resetImageViewSize(R.id.color1U,view)
            resetImageViewSize(R.id.color3U,view)
            resetImageViewSize(R.id.color4U,view)
            resetImageViewSize(R.id.color5U,view)
            resetImageViewSize(R.id.color6U,view)
        }
        binding.color3U.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color3))
            barcolor(R.color.color3,requireActivity(),requireContext())
            colorr="#91F48F"
            setImageViewSize(165,165, R.id.color3U,view)
            resetImageViewSize(R.id.color1U,view)
            resetImageViewSize(R.id.color2U,view)
            resetImageViewSize(R.id.color4U,view)
            resetImageViewSize(R.id.color5U,view)
            resetImageViewSize(R.id.color6U,view)
        }
        binding.color4U.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color4))
            barcolor(R.color.color4,requireActivity(),requireContext())
            colorr="#FD99FF"
            setImageViewSize(165,165, R.id.color4U,view)
            resetImageViewSize(R.id.color1U,view)
            resetImageViewSize(R.id.color2U,view)
            resetImageViewSize(R.id.color3U,view)
            resetImageViewSize(R.id.color5U,view)
            resetImageViewSize(R.id.color6U,view)
        }
        binding.color5U.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color5))
            barcolor(R.color.color5,requireActivity(),requireContext())
            colorr="#9EFFFF"
            setImageViewSize(165,165, R.id.color5U,view)
            resetImageViewSize(R.id.color1U,view)
            resetImageViewSize(R.id.color2U,view)
            resetImageViewSize(R.id.color3U,view)
            resetImageViewSize(R.id.color4U,view)
            resetImageViewSize(R.id.color6U,view)
        }
        binding.color6U.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color6))
            barcolor(R.color.color6,requireActivity(),requireContext())
            colorr="#FFFFFF"
            setImageViewSize(165,165, R.id.color6U,view)
            resetImageViewSize(R.id.color1U,view)
            resetImageViewSize(R.id.color2U,view)
            resetImageViewSize(R.id.color3U,view)
            resetImageViewSize(R.id.color4U,view)
            resetImageViewSize(R.id.color5U,view)
        }

    }
}