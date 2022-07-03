package com.example.week1

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri

class ImageDialog(context: Context) {

    private val dialog = Dialog(context)
    private lateinit var imageView_img : ImageView
    private lateinit var button_back : ImageButton
    private lateinit var onClickListener: BtnClickListener

    fun setOnClickListener(listener: BtnClickListener)
    {
        onClickListener = listener
    }

    fun showDialog(img : String)
    {
        dialog.setContentView(R.layout.image_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        imageView_img = dialog.findViewById<ImageView>(R.id.dialog_img)
        button_back = dialog.findViewById<ImageButton>(R.id.btn_back)

        Log.d("img", "img = " + img)
        imageView_img.setImageURI(img.toUri())

        dialog.show()

        button_back.setOnClickListener {
            onClickListener.onClicked("yes")
            dialog.dismiss()
        }
    }

    interface BtnClickListener
    {
        fun onClicked(change: String)
    }

}