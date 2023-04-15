package com.ihsanarslan.mynotes.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.databinding.FragmentReadCloudNoteBinding
import com.ihsanarslan.mynotes.util.barcolor

class ReadCloudNoteFragment : Fragment() {
    private lateinit var binding: FragmentReadCloudNoteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReadCloudNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //update fragmanına gelen verileri kullandık burada
        arguments?.let {
            val title = ReadCloudNoteFragmentArgs.fromBundle(it).title
            val content = ReadCloudNoteFragmentArgs.fromBundle(it).content
            val color = ReadCloudNoteFragmentArgs.fromBundle(it).color
            binding.titleRead.text = title
            binding.contentRead.text = content
            binding.background.setBackgroundColor(color)

            when (color) {
                -24930 -> {barcolor(R.color.color1,requireActivity(),requireContext())}
                -2663 -> {barcolor(R.color.color2,requireActivity(),requireContext())}
                -7211889 -> {barcolor(R.color.color3,requireActivity(),requireContext())}
                -157185 -> {barcolor(R.color.color4,requireActivity(),requireContext())}
                -6356993 -> {barcolor(R.color.color5,requireActivity(),requireContext())}
                -1 -> {barcolor(R.color.color6,requireActivity(),requireContext())}
                else -> barcolor(R.color.color2,requireActivity(),requireContext())
            }
        }
        binding.backButtonR.setOnClickListener {
            val action=ReadCloudNoteFragmentDirections.actionReadCloudNoteFragmentToCloudFragment()
            Navigation.findNavController(view).navigate(action)
        }

    }

}