package com.example.week1

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ContactDialog(context: Context) {

    private val dialog = Dialog(context)
    private val context = context
    private lateinit var imageView_img : ImageView
    private lateinit var textView_name : TextView
    private lateinit var textView_number : TextView
    private lateinit var button_back : ImageButton
    private lateinit var button_call : ImageButton
    private lateinit var button_profile : ImageButton
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
        button_back = dialog.findViewById<ImageButton>(R.id.btn_back)
        button_call = dialog.findViewById<ImageButton>(R.id.btn_call)
        button_profile = dialog.findViewById<ImageButton>(R.id.btn_profile)

//        Log.d("img", "img = " + img)
        val option1 = RequestOptions().circleCrop()
        Glide.with(context).load(img).centerCrop().apply(option1).into(imageView_img)
//        imageView_img.setImageURI(img.toUri())
        textView_name.text = name
        textView_number.text = number

        dialog.show()

        imageView_img.setOnClickListener{
            onClickListener.onClicked("showimage")
            dialog.dismiss()
        }

        button_profile.setOnClickListener{
            onClickListener.onClicked("yes")
            dialog.dismiss()
        }

        button_call.setOnClickListener {
            val uri = Uri.parse("tel:"+ number)
            var intent = Intent(Intent.ACTION_CALL, uri)
//            Log.d("phone", "tel:"+ number)

            if(intent.resolveActivity(context.packageManager) != null){
                context.startActivity(intent)
            }
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