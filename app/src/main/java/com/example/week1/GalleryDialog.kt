package com.example.week1

import android.app.Dialog
import android.content.Context
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.l4digital.fastscroll.FastScrollView

class GalleryDialog(context: Context) {

    private val dialog = Dialog(context)
    val context = context
    private lateinit var recyclerView : FastScrollView
    private val uriArr: ArrayList<String> = ArrayList()
    private lateinit var buttonOk : ImageButton
    private lateinit var buttonBack : ImageButton
    var uri : String = "none"
    var prevuri : String = "none"
    private lateinit var onClickListener: ItemClickListener

    private lateinit var rvLayoutManager: GridLayoutManager
    private lateinit var rvAdapter: GalleryDialogAdapter

    fun setOnClickListener(listener: ItemClickListener)
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

        recyclerView = dialog.findViewById(R.id.recyclerViewProfile)

        loadImage()

        rvLayoutManager = GridLayoutManager(context, 3)
        rvAdapter = GalleryDialogAdapter(context, uriArr)

        recyclerView.apply {
            setLayoutManager(rvLayoutManager)
            setAdapter(rvAdapter)
        }

        buttonBack = dialog.findViewById(R.id.btn_back)
        buttonOk = dialog.findViewById(R.id.btn_ok)
//
        dialog.show()



        rvAdapter.setItemClickListener(object : GalleryDialogAdapter.OnItemClickListener{
            override fun onClick(v: View, position : Int){
                uri = uriArr[position]
                if(prevuri == uri){
                    uri = "none"
                }
                prevuri = uri
            }
        })

        buttonOk.setOnClickListener {
            onClickListener.onClicked(uri)
            if(uri != "none")
                dialog.dismiss()
        }
//
        buttonBack.setOnClickListener {
            uri = "cancel"
            onClickListener.onClicked(uri)
            dialog.dismiss()
        }
    }

    interface ItemClickListener
    {
        fun onClicked(uri: String){

        }
    }

}