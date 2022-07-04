package com.example.week1

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.net.toUri

class ImageDialog(context: Context) {

    private val dialog = Dialog(context)
    private lateinit var imageViewImg : ImageView
    private lateinit var buttonBack : ImageButton
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

        imageViewImg = dialog.findViewById(R.id.dialog_img)
        buttonBack = dialog.findViewById(R.id.btn_back)

//        Log.d("img", "img = " + img)
        imageViewImg.setImageURI(img.toUri())

        dialog.show()

        buttonBack.setOnClickListener {
            onClickListener.onClicked("yes")
            dialog.dismiss()
        }
    }

    interface BtnClickListener
    {
        fun onClicked(change: String)
    }

}