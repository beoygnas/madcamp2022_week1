package com.example.week1

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.week1.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val tabIcon = listOf(
        R.drawable.noun_contact,
        R.drawable.noun_gallery,
        R.drawable.noun_callendar
    )
    private val MY_PERMISSION_ACCESS_ALL = 100
    private var permissions = arrayOf(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.CALL_PHONE
        // For future Permissions
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
                    tab.text = "Contact"
                    tab.setIcon(tabIcon[position])
                }
                1 -> {
                    tab.text = "Gallery"
                    tab.setIcon(tabIcon[position])
                }
                2 -> {
                    tab.text = "Callendar"
                    tab.setIcon(tabIcon[position])
                }
            }
        }.attach()
        getPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_PERMISSION_ACCESS_ALL) {
            if(grantResults.isNotEmpty()) {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) showPermissionExplanationDialog() //System.exit(0) //toast("권한 거부 됨")
                }
            }
        }
    }

    private fun getPermission() {

        val needPermissions: MutableList<String> = emptyArray<String>().toMutableList()

        for (permission in permissions) {
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(permission)

//                Log.d("TAG", "########## need permission")
            }
        }
//        ActivityCompat.shouldShowRequestPermissionRationale(this, )
//        과거에 거절한 적있는지 필요한 경우
        if (needPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, needPermissions.toTypedArray(), MY_PERMISSION_ACCESS_ALL)
        }
    }

    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(this)
            .setMessage("권한을 켜주셔야지 앱을 사용할 수 있습니다. 앱 설정 화면으로 진입하셔서 권한을 켜주세요.")
            .setPositiveButton("권한 허용하러 가기") { _, _ -> getPermission() } // 나중엔 실제 앱처럼 설정가서 바꾸도록 우회?
            .setNegativeButton("앱 종료하기") { _, _ -> finish() }
            .show()
    }
}