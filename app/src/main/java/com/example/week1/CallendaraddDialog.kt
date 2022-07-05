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

class CallendaraddDialog(context: Context) {

    private val dialog = Dialog(context)
    private val context = context
    private lateinit var editView : EditText
    private lateinit var textView : TextView
    private lateinit var button_back : ImageButton
    private lateinit var button_ok : ImageButton
    private lateinit var onClickListener: BtnClickListener

    fun convertTimestampToDate(timestamp: Long) : String {
        val sdf = SimpleDateFormat("yyyyMMddhhmmss")
        val date = sdf.format(timestamp)
        return date
    }

    fun setOnClickListener(listener: BtnClickListener)
    {
        onClickListener = listener
    }

    fun showDialog(year : Int, month : Int, day : Int)
    {
        dialog.setContentView(R.layout.callendaradd_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        textView = dialog.findViewById<TextView>(R.id.textview1)
        editView = dialog.findViewById<EditText>(R.id.edittext)
        button_ok = dialog.findViewById<ImageButton>(R.id.btn_ok)
        button_back = dialog.findViewById<ImageButton>(R.id.btn_back)


        textView.text = String.format("%d년 %d월 %d일의 메모입니다.\n", year, month, day)

        dialog.show()

        button_ok.setOnClickListener {
            val currentTime = System.currentTimeMillis()

            var content = editView.text.toString()
            var regdata = convertTimestampToDate(currentTime)

            onClickListener.onClicked(content, regdata)
            dialog.dismiss()
        }

        button_back.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface BtnClickListener
    {
        fun onClicked(content: String, regdata : String)
    }

}