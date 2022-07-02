package com.example.week1

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ContactDialog(context: Context) {

    private val dialog = Dialog(context)
    private val context = context
    private lateinit var imageView_img : ImageView
    private lateinit var textView_name : TextView
    private lateinit var textView_number : TextView
    private lateinit var button_back : Button
    private lateinit var button_call : Button
    private lateinit var onClickListener: BtnClickListener

    fun setOnClickListener(listener: BtnClickListener)
    {
        onClickListener = listener
    }

    fun showDialog(img : String, name:String, number:String)
    {
        dialog.setContentView(R.layout.contact_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)


        imageView_img = dialog.findViewById<ImageView>(R.id.dialog_img)
        textView_name = dialog.findViewById<TextView>(R.id.dialog_name)
        textView_number = dialog.findViewById<TextView>(R.id.dialog_number)
        button_back = dialog.findViewById<Button>(R.id.btn_back)
        button_call = dialog.findViewById<Button>(R.id.btn_call)

        Log.d("img", "img = " + img)
        val option1 = RequestOptions().circleCrop()
        Glide.with(context).load(img).centerCrop().apply(option1).into(imageView_img)
//        imageView_img.setImageURI(img.toUri())
        textView_name.text = name
        textView_number.text = number

        dialog.show()

        imageView_img.setOnClickListener{
            onClickListener.onClicked("yes")
            dialog.dismiss()
        }

        button_call.setOnClickListener {
            dialog.dismiss()
        }
//
        button_back.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface BtnClickListener
    {
        fun onClicked(change: String)
    }

}