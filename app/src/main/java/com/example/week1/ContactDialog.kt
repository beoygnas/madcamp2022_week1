package com.example.week1

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ContactDialog(context: Context) {

    private val dialog = Dialog(context)
    private val context = context
    private lateinit var imageViewImg : ImageView
    private lateinit var textViewName : TextView
    private lateinit var textViewNumber : TextView
    private lateinit var buttonBack : ImageButton
    private lateinit var buttonCall : ImageButton
    private lateinit var buttonProfile : ImageButton
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


        imageViewImg = dialog.findViewById(R.id.dialog_img)
        textViewName = dialog.findViewById(R.id.dialog_name)
        textViewNumber = dialog.findViewById(R.id.dialog_number)
        buttonBack = dialog.findViewById(R.id.btn_back)
        buttonCall = dialog.findViewById(R.id.btn_call)
        buttonProfile = dialog.findViewById(R.id.btn_profile)

//        Log.d("img", "img = " + img)
        val option1 = RequestOptions().circleCrop()
        Glide.with(context).load(img).centerCrop().apply(option1).into(imageViewImg)
//        imageView_img.setImageURI(img.toUri())
        textViewName.text = name
        textViewNumber.text = number

        dialog.show()

        imageViewImg.setOnClickListener{
            onClickListener.onClicked("showimage")
            dialog.dismiss()
        }

        buttonProfile.setOnClickListener{
            onClickListener.onClicked("yes")
            dialog.dismiss()
        }


        buttonCall.setOnClickListener {
            val uri = Uri.parse("tel:$number")
            val intent = Intent(Intent.ACTION_CALL, uri)
//            Log.d("phone", "tel:"+ number)

            if(intent.resolveActivity(context.packageManager) != null){
                context.startActivity(intent)
            }
            dialog.dismiss()
        }
//
        buttonBack.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface BtnClickListener
    {
        fun onClicked(change: String)
    }

}