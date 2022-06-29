package com.example.week1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.week1.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val tabIcon = listOf(
        R.drawable.noun_contact,
        R.drawable.noun_gallery
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.apply {
            adapter = MyPagerAdapter(context as FragmentActivity)
        }

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Contact"
                    tab.setIcon(tabIcon[position])
                }
                1 -> {
                    tab.text = "Gallery"
                    tab.setIcon(tabIcon[position])
                }
                else -> tab.text = "TBD"
            }
        }.attach()
    }
}