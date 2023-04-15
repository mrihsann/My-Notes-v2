package com.ihsanarslan.mynotes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ihsanarslan.mynotes.view.RegisterFragment
import com.ihsanarslan.mynotes.view.signinFragment

//kullanıcı giriş-kayıt sayfasının, tabLayout adapteri
class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position==0) {
            signinFragment()
        }
        else {
            RegisterFragment()
        }
    }

}