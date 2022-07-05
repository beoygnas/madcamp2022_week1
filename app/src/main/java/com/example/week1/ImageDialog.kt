package com.example.week1

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide

class ImageDialog(context: Context) {

    private val dialog = Dialog(context)
    val context = context
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
//        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val display = context.resources.displayMetrics
        dialog.window!!.setLayout(display.widthPixels*7/8, display.heightPixels*3/4)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        imageViewImg = dialog.findViewById(R.id.dialog_img)
        buttonBack = dialog.findViewById(R.id.btn_back)

//        Log.d("img", "img = " + img)
//        imageViewImg.setImageURI(img.toUri())
        Glide.with(context).load(img).into(imageViewImg)
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