package com.example.week1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var arrStr: ArrayList<String> = ArrayList()
        for (i in 1..100) {
            arrStr.add(i.toString())
        }

        var adapter = ViewPagerAdapter(arrStr)

        viewPager2 = findViewById(R.id.viewPage2)
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager2.adapter = adapter

    }
}