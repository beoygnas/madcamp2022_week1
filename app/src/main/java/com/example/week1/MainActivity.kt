package com.example.week1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter;
import android.widget.ListView;

class MainActivity : AppCompatActivity() {

    var phoneAdapter = phoneAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val phonelistView = findViewById<ListView>(R.id.phonelistview)
        phonelistView.adapter = phoneAdapter
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("김상엽", "010-2647-4429")
        phoneAdapter.addItem("이상민", "010-7588-6085")
    }
}
