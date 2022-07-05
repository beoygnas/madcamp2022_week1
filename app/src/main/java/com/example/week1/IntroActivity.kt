package com.example.week1

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.security.Permission


class IntroActivity: AppCompatActivity() {

    private val MY_PERMISSION_ACCESS_ALL = 100
    private var permissions = arrayOf(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.CALL_PHONE
        // For future Permissions
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        onCheckPermission()

    }


    override fun onStop() {
        super.onStop()
        finish()
    }

    private fun onCheckPermission() {
        val needPermissions: ArrayList<String> = ArrayList()
        val newRequests: ArrayList<String> = ArrayList()
        val failedRequests: ArrayList<String> = ArrayList()

        for (permission in permissions) {
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(permission)
            }
        }
        // 필요한 Permission request가 있다
        if(needPermissions.isNotEmpty()) {
            for (permission in needPermissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    failedRequests.add(permission)
                }
                else {
                    newRequests.add(permission)
                }
            }
            if (newRequests.isNotEmpty())
                ActivityCompat.requestPermissions(this, newRequests.toTypedArray(), MY_PERMISSION_ACCESS_ALL)
            for (permission in failedRequests) {
                println("Failed Requests")
                when(permission) {
                    permissions[0] -> showPermissionExplanationDialog("연락처")
                    permissions[1] -> showPermissionExplanationDialog("파일 및 미디어")
                    permissions[2] -> showPermissionExplanationDialog("전화")
                }
                // 후에 추가
            }
        }
        else {
            gotoMain()
        }
    }

    private fun gotoMain() {
        var handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()

        }, 700)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_PERMISSION_ACCESS_ALL) {
            if(grantResults.isNotEmpty()) {
                for(i:Int in 0..2) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        when(permissions[i]) {
                            android.Manifest.permission.READ_CONTACTS -> showPermissionExplanationDialog("연락처")
                            android.Manifest.permission.READ_EXTERNAL_STORAGE -> showPermissionExplanationDialog("파일 및 미디어")
                            android.Manifest.permission.CALL_PHONE -> showPermissionExplanationDialog("전화")
                        }
                    }
                    // 한번 변경하고 나서 체크?
                    onCheckPermission()
                }
            }
        }
    }


    private fun showPermissionExplanationDialog(permission: String) {
        AlertDialog.Builder(this)
            .setMessage("$permission 권한을 켜주셔야지 앱을 사용할 수 있습니다. 앱 설정 화면으로 진입하셔서 권한을 켜주세요.")
            .setPositiveButton("권한 허용하러 가기") { _, _ ->
                val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:" + this.packageName))
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                this.finish() }
            .setNegativeButton("앱 종료하기") { _, _ -> finish() }
            .show()
    }

}