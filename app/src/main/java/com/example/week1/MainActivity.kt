package com.example.week1

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.week1.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val tabIcon = listOf(
        R.drawable.noun_contact,
        R.drawable.noun_gallery,
        R.drawable.noun_callendar
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Action Bar -> Tools gets blocked
//        setSupportActionBar(binding.topAppBar)


        binding.viewpager.apply {
            adapter = MyPagerAdapter(context as FragmentActivity)
        }
//        binding.viewpager.isUserInputEnabled = false;
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "연락처"
                    tab.setIcon(tabIcon[position])
                }
                1 -> {
                    tab.text = "갤러리"
                    tab.setIcon(tabIcon[position])
                }
                2 -> {
                    tab.text = "캘린더"
                    tab.setIcon(tabIcon[position])
                }
            }
        }.attach()
    }

}