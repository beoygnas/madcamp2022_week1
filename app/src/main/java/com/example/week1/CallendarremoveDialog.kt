package com.example.week1

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat

class CallendarremoveDialog(context: Context) {

    private val dialog = Dialog(context)
    private val context = context
    private lateinit var button_back : ImageButton
    private lateinit var button_ok : ImageButton
    private lateinit var onClickListener: BtnClickListener


    fun setOnClickListener(listener: BtnClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.callendarremove_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        button_ok = dialog.findViewById<ImageButton>(R.id.btn_ok)
        button_back = dialog.findViewById<ImageButton>(R.id.btn_back)

        dialog.show()

        button_ok.setOnClickListener {
            onClickListener.onClicked("yes")
            dialog.dismiss()
        }

        button_back.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface BtnClickListener
    {
        fun onClicked(content: String)
    }

}