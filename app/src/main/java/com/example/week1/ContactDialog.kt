package com.example.week1

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ContactDialog(context: Context) {

    private val dialog = Dialog(context)
    private lateinit var imageView_img : ImageView
    private lateinit var textView_name : TextView
    private lateinit var textView_number : TextView
    private lateinit var button_back : Button
    private lateinit var button_profile : Button
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog(img : Int, name:String, number:String)
    {
        dialog.setContentView(R.layout.contact_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        imageView_img = dialog.findViewById<ImageView>(R.id.dialog_img)
        textView_name = dialog.findViewById<TextView>(R.id.dialog_name)
        textView_number = dialog.findViewById<TextView>(R.id.dialog_number)
        button_back = dialog.findViewById<Button>(R.id.btn_back)
        button_profile = dialog.findViewById<Button>(R.id.btn_profile)

        Log.d("img", "img = " + img)
        imageView_img.setImageResource(img)
        textView_name.text = name
        textView_number.text = number

        dialog.show()

        button_profile.setOnClickListener {
            dialog.dismiss()
        }
//
        button_back.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }

}