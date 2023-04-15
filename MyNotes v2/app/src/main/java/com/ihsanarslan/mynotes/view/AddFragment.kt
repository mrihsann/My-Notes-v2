package com.ihsanarslan.mynotes.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.service.NoteDao
import com.ihsanarslan.mynotes.databinding.FragmentAddBinding
import com.ihsanarslan.mynotes.noteDatabase
import com.ihsanarslan.mynotes.util.barcolor
import com.ihsanarslan.mynotes.util.resetImageViewSize
import com.ihsanarslan.mynotes.util.setImageViewSize
import com.ihsanarslan.mynotes.viewmodel.AddViewModel


class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var noteDao: NoteDao
    private lateinit var viewModel: AddViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view modelimizi oluşturuyoruz.
        viewModel = ViewModelProvider(this)[AddViewModel::class.java]
        noteDao = noteDatabase.noteDao()
        //renk butonlarına tıklanınca seçilen rengi tutuyoruz burada. Not eklendiği zaman bu rengi kullanacağız.
        var colorr="#FFF599"

        //fragment ilk açılıdğında bar rengini değiştiriyoruz ki arka plan rengiyle aynı olsun
        barcolor(R.color.color2,requireActivity(),requireContext())

        //toolBar içinde ki butonlara tıklandığı zaman yapılacak işlemler
        binding.toolBar.backButton.setOnClickListener {
            backAlert(view,requireContext())
        }
        binding.toolBar.addbutton.setOnClickListener {
            val titlee=binding.addTitle.text
            val contentt=binding.addContent.text

            if (titlee.isNotEmpty()){
                viewModel.addNote(titlee.toString(),contentt.toString(),colorr,noteDao)
                val action=AddFragmentDirections.actionAddFragmentToListFragment()
                Navigation.findNavController(it).navigate(action)
            }
            else{
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.title_not_found)
                builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
                builder.setMessage(R.string.back_alert_content_title_not_found)
                    .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                    .setPositiveButton(R.string.yes) { dialog, id ->
                        dialog.cancel()
                    }
                    .setNegativeButton(R.string.no) { dialog, id ->
                        val action= AddFragmentDirections.actionAddFragmentToListFragment()
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
        binding.toolBar.colors.setOnClickListener {
            if (binding.colors.visibility == View.GONE) {
                binding.colors.visibility = View.VISIBLE
            } else {
                binding.colors.visibility = View.GONE
            }
        }

        //renk butonlarına tıklandığı zaman arka plan rengini değiştiriyoruz
        binding.color1.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color1))
            colorr="#FF9E9E"
            barcolor(R.color.color1,requireActivity(),requireContext())
            setImageViewSize(165,165,R.id.color1,view)
            resetImageViewSize(R.id.color2,view)
            resetImageViewSize(R.id.color3,view)
            resetImageViewSize(R.id.color4,view)
            resetImageViewSize(R.id.color5,view)
            resetImageViewSize(R.id.color6,view)

        }
        binding.color2.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color2))
            colorr="#FFF599"
            barcolor(R.color.color2,requireActivity(),requireContext())
            setImageViewSize(165,165,R.id.color2,view)
            resetImageViewSize(R.id.color1,view)
            resetImageViewSize(R.id.color3,view)
            resetImageViewSize(R.id.color4,view)
            resetImageViewSize(R.id.color5,view)
            resetImageViewSize(R.id.color6,view)
        }
        binding.color3.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color3))
            colorr="#91F48F"
            barcolor(R.color.color3,requireActivity(),requireContext())
            setImageViewSize(165,165,R.id.color3,view)
            resetImageViewSize(R.id.color1,view)
            resetImageViewSize(R.id.color2,view)
            resetImageViewSize(R.id.color4,view)
            resetImageViewSize(R.id.color5,view)
            resetImageViewSize(R.id.color6,view)
        }
        binding.color4.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color4))
            colorr="#FD99FF"
            barcolor(R.color.color4,requireActivity(),requireContext())
            setImageViewSize(165,165,R.id.color4,view)
            resetImageViewSize(R.id.color1,view)
            resetImageViewSize(R.id.color2,view)
            resetImageViewSize(R.id.color3,view)
            resetImageViewSize(R.id.color5,view)
            resetImageViewSize(R.id.color6,view)
        }
        binding.color5.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color5))
            colorr="#9EFFFF"
            barcolor(R.color.color5,requireActivity(),requireContext())
            setImageViewSize(165,165,R.id.color5,view)
            resetImageViewSize(R.id.color1,view)
            resetImageViewSize(R.id.color2,view)
            resetImageViewSize(R.id.color3,view)
            resetImageViewSize(R.id.color4,view)
            resetImageViewSize(R.id.color6,view)
        }
        binding.color6.setOnClickListener {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color6))
            colorr="#FFFFFF"
            barcolor(R.color.color6,requireActivity(),requireContext())
            setImageViewSize(165,165,R.id.color6,view)
            resetImageViewSize(R.id.color1,view)
            resetImageViewSize(R.id.color2,view)
            resetImageViewSize(R.id.color3,view)
            resetImageViewSize(R.id.color4,view)
            resetImageViewSize(R.id.color5,view)
        }
    }
    fun backAlert(view: View,context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.back_alert_title_add_fragment)
        builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
        builder.setMessage(R.string.back_alert_content_add_fragment)
            .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
            .setPositiveButton(R.string.yes) { dialog, id ->
                // Evet'e tıklandığında yapılacak işlemler buraya yazılır
                val action= AddFragmentDirections.actionAddFragmentToListFragment()
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
}