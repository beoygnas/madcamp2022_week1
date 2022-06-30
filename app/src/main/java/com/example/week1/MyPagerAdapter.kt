package com.example.week1

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val NUM_PAGES = 3
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> { ContactFragment.newInstance("Contact", "") }
            1 -> { GalleryFragment.newInstance("Gallery", "") }
            else -> { MyFragment.newInstance("TBD", "") }
        }
    }

}

