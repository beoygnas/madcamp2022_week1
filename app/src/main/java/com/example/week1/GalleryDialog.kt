package com.example.week1

import android.app.Dialog
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleryDialog(context: Context) {

    private val dialog = Dialog(context)
    val context = context
    private lateinit var recyclerAdapter : GalleryDialogAdapter
    private lateinit var recyclerView : RecyclerView
    private val uriArr: ArrayList<String> = ArrayList<String>()
    private lateinit var button_ok : Button
    private lateinit var button_back : Button
    private lateinit var uri : String
    private lateinit var onClickListener: itemClickListener

    fun setOnClickListener(listener: itemClickListener)
    {
        onClickListener = listener
    }

    private fun loadImage() {
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")

        if(cursor!=null){
            while(cursor.moveToNext()){
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                uriArr.add(uri)
            }
            cursor.close()
        }
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.gallery_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewProfile)

        loadImage()

        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerAdapter = GalleryDialogAdapter(context, uriArr)
        recyclerView.adapter = recyclerAdapter
        button_back = dialog.findViewById<Button>(R.id.btn_back)
        button_ok = dialog.findViewById<Button>(R.id.btn_ok)
//
        dialog.show()

        recyclerAdapter.setItemClickListener(object : GalleryDialogAdapter.OnItemClickListener{
            override fun onClick(v: View, position : Int){
                //uri 방해한거 풀기

                //새로 uri 받기
                uri = uriArr[position]
//                사진 uri 타고 imageview를 방해.
                //uri 방해하기
            }
        })

        button_ok.setOnClickListener {
            onClickListener.onClicked(uri)
            dialog.dismiss()
        }
//
        button_back.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface itemClickListener
    {
        fun onClicked(uri: String){

        }
    }

}