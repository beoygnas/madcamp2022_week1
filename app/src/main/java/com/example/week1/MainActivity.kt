package com.example.week1

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.week1.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val tabIcon = listOf(
        R.drawable.noun_contact,
        R.drawable.noun_gallery
    )
    val MY_PERMISSION_ACCESS_ALL = 100
    var permissions = arrayOf(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
        // For future Permissions
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

        val need_permissions: MutableList<String> = emptyArray<String>().toMutableList()

        for (permission in permissions) {
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                need_permissions.add(permission)

//                Log.d("TAG", "########## need permission")
            }
        }
//        ActivityCompat.shouldShowRequestPermissionRationale(this, )
//        과거에 거절한 적있는지 필요한 경우
        if (need_permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, need_permissions.toTypedArray(), MY_PERMISSION_ACCESS_ALL)
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