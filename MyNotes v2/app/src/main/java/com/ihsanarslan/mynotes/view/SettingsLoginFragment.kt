package com.ihsanarslan.mynotes.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.service.NoteDao
import com.ihsanarslan.mynotes.service.TrashDao
import com.ihsanarslan.mynotes.databinding.FragmentSettingsLoginBinding
import com.ihsanarslan.mynotes.noteDatabase
import com.ihsanarslan.mynotes.viewmodel.SettingsViewModel

class SettingsLoginFragment : Fragment() {
    private lateinit var trashDao: TrashDao
    private lateinit var noteDao: NoteDao
    private lateinit var binding: FragmentSettingsLoginBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteDao = noteDatabase.noteDao()
        trashDao = noteDatabase.trashDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        // SharedPreferences'tan veri okuma
        val sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString("userName", "Kanka")
        //okunan veriyi işliyoruz.
        binding.userNameLog.text = value

        //back button
        binding.backSettinglog.setOnClickListener{
            val action=SettingsLoginFragmentDirections.actionSettingsLoginFragmentToListFragment()
            Navigation.findNavController(view).navigate(action)
        }

        //menü kısmı
        binding.contactlayoutlog.setOnClickListener {

            val recipient = "thearslanihsan@gmail.com"

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            }
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }

        binding.deletenotes.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.warning)
            builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
            builder.setMessage(R.string.delete_notes)
                .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                .setPositiveButton(R.string.yes) { dialog, id ->
                    // Evet'e tıklandığında yapılacak işlemler buraya yazılır
                    viewModel.notToTrashTransport(noteDao, trashDao)
                    Toast.makeText(requireContext(),R.string.toast_delete_note, Toast.LENGTH_LONG).show()
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

        binding.deletetrashnotes.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.warning)
            builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
            builder.setMessage(R.string.delete_trash)
                .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                .setPositiveButton(R.string.yes) { dialog, id ->
                    // Evet'e tıklandığında yapılacak işlemler buraya yazılır
                    viewModel.deleteTrashNotes(trashDao)
                    Toast.makeText(requireContext(),R.string.toast_delete_trash_all, Toast.LENGTH_LONG).show()
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

        binding.recovertrashnotes.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.warning)
            builder.setIcon(R.drawable.ic_baseline_warning_amber_24)
            builder.setMessage(R.string.save_notes)
                .setCancelable(true) //ekranda biryere tıklayınca uyarı mesajının kapanabilmesini ayarlıyoruz.
                .setPositiveButton(R.string.yes) { dialog, id ->
                    // Evet'e tıklandığında yapılacak işlemler buraya yazılır
                    viewModel.recoverTrashNotes(trashDao,noteDao)
                    Toast.makeText(requireContext(),R.string.saved_notes, Toast.LENGTH_LONG).show()
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
            alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED)
        }

        binding.aboutlayoutlog.setOnClickListener {

            val url = "https://www.linkedin.com/in/ihsanarslan/"

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }
        binding.secPrivLayoutLog.setOnClickListener {

            val url = "https://ihsanarslan.com.tr/my-notes-gizlilik-sozlesmesi/"

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }
        binding.loginButtonLayout.setOnClickListener {
            val action=SettingsLoginFragmentDirections.actionSettingsLoginFragmentToLoginFragment()
            Navigation.findNavController(view).navigate(action)
        }

        binding.cloudSettingsLogin.setOnClickListener {
            val action=SettingsLoginFragmentDirections.actionSettingsLoginFragmentToLoginFragment()
            Navigation.findNavController(view).navigate(action)
        }

        //adı değiştirme kısmı
        binding.editnamelog.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .show()

            val buttonSave = dialogView.findViewById<Button>(R.id.button_save)
            buttonSave.setOnClickListener {
                val userName=dialogView.findViewById<EditText>(R.id.edit_text).text.toString()
                val sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("userName", "$userName")
                editor.apply()
                val action=SettingsLoginFragmentDirections.actionSettingsLoginFragmentSelf()
                Navigation.findNavController(view).navigate(action)
                dialog.cancel()
            }
        }
    }
}