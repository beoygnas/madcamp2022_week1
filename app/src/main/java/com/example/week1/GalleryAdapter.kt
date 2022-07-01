package com.example.week1

import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.week1.databinding.GalleryItemBinding


class GalleryAdapter(
//    var imageList: List<Image>,
//    private val listener: (Image, Int) -> Unit
    var imageList: List<String>,
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: GalleryItemBinding) : RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // bind the items with each item
    // of the list languageList
    // which than will be
    // shown in recycler view
    // to keep it simple we are
    // not setting any image data to view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(imageList[position]){
//                binding.galleryItem.setImageResource(this.img)
//                Log.d("Uri:################ ", this)
                binding.galleryItem.setImageURI(this.toUri())
            }
        }

//        holder.itemView.setOnClickListener { listener(imageList[position], position) }
//        println(holder.itemView)
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return imageList.size
    }
}
