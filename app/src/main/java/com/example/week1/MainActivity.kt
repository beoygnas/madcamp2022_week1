package com.example.week1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter;
import android.widget.ListView;


class MainActivity : AppCompatActivity() {

    val LIST_MENU = arrayOf("LIST1", "LIST2", "LIST3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU)

        val listview = findViewById(R.id.listview1) as ListView
        listview.setAdapter(adapter)
    }

}