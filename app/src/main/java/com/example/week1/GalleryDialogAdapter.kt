package com.example.week1

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.week1.databinding.GalleryItemBinding
import org.jetbrains.anko.padding

class GalleryDialogAdapter(
    val context: Context,
    var imageList: List<String>
) : RecyclerView.Adapter<GalleryDialogAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: GalleryItemBinding) : RecyclerView.ViewHolder(binding.root)


    var previmageview : ImageView ?= null

    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(imageList[position]){
//                val display = context.resources.displayMetrics
                binding.galleryItem.padding = 2
                binding.itemWrapper.layoutParams = ConstraintLayout.LayoutParams(dpToPx(context, 110f).toInt(), dpToPx(context, 110f).toInt())
                Glide.with(context).load(this).centerCrop().into(binding.galleryItem)
            }
        }

        holder.itemView.setOnClickListener{

            val imgview = it.findViewById<ImageView>(R.id.gallery_item)

            if(imgview == previmageview){
                imgview.colorFilter = null
                previmageview = null
            }
            else if(imgview != previmageview) {
                imgview.setColorFilter(Color.parseColor("#80000000"))
                previmageview?.colorFilter = null
                previmageview = imgview
            }
            itemClickListener.onClick(it, position)
        }
    }

    interface OnItemClickListener{
        fun onClick(v: View, position:Int){

        }
    }

    fun setItemClickListener(OnItemClickListener : OnItemClickListener){
        this.itemClickListener = OnItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener


    override fun getItemCount(): Int {
        return imageList.size
    }
}
