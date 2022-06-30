package com.example.week1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.week1.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.week1.databinding.FragmentMyBinding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var phoneAdapter = phoneAdapter()

    private val tabIcon = listOf(
        R.drawable.noun_contact,
        R.drawable.noun_gallery
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val mybinding = FragmentMyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.apply {
            adapter = MyPagerAdapter(context as FragmentActivity)
        }

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Contact"
                    tab.setIcon(tabIcon[position])
                    mybinding.textview.text = "바인딩 성공"

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