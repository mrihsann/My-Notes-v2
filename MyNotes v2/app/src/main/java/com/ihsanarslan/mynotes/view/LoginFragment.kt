package com.ihsanarslan.mynotes.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.ihsanarslan.mynotes.R
import com.ihsanarslan.mynotes.adapter.FragmentPageAdapter
import com.ihsanarslan.mynotes.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var tabLayout:TabLayout
    private lateinit var viewPager2:ViewPager2
    private lateinit var adapter: FragmentPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backLogin.setOnClickListener {
            val action=LoginFragmentDirections.actionLoginFragmentToSettingsLoginFragment()
            Navigation.findNavController(view).navigate(action)
        }

        //tabLayout ayarları
        tabLayout=binding.tablayout
        viewPager2=binding.viewpager2
        adapter=FragmentPageAdapter(requireActivity().supportFragmentManager,lifecycle)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.login))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.register))
        viewPager2.adapter=adapter
        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!=null){
                    viewPager2.currentItem=tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}