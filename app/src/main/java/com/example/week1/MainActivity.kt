package com.example.week1

import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.SimpleCursorAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.week1.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val tabIcon = listOf(
        R.drawable.noun_contact,
        R.drawable.noun_gallery
    )

    val MY_PERMISSION_ACCESS_ALL = 100
    var phonelist = ArrayList<Phone>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle = Bundle()
        bundle.putString("hello", "hello")
        var CF = ContactFragment()
        CF.arguments = bundle

//        val viewPager : ViewPager2 = findViewById(R.id.viewpager)
//        val viewpagerFragmentAdapter = MyPagerAdapter(this)
//        viewPager.adapter = viewpagerFragmentAdapter

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

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            var permissions = arrayOf(
                android.Manifest.permission.READ_CONTACTS
            // For future Permissions
            )
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSION_ACCESS_ALL)
        }

        phonelist = read()
        print(phonelist)
    }

    fun read() : ArrayList<Phone>{
        val list = ArrayList<Phone>()
        val listurl = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projections = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            , ContactsContract.CommonDataKinds.Phone.NUMBER)
        var cursor = contentResolver.query(listurl, projections, null, null, null)

        while(cursor?.moveToNext()?:false) {
            val id = cursor?.getString(0)
            var name = cursor?.getString(1)
            var number = cursor?.getString(2)
            // 개별 전화번호 데이터 생성
            val phone = Phone(ContextCompat.getDrawable(this, R.drawable.img)!!, name, number)
            // 결과목록에 더하기
            list.add(phone)
            print("으아아")
        }
        return list
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode === MY_PERMISSION_ACCESS_ALL) {
            if(grantResults.isNotEmpty()) {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) System.exit(0)
                }
            }
        }
    }
}