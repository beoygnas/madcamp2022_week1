package com.example.week1

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.week1.databinding.CallenderItem2Binding
import com.example.week1.databinding.CallenderItemBinding
import com.l4digital.fastscroll.FastScroller


class callendarAdapter(
    val context: CallendarFragment,
    private val items: ArrayList<Schedule>,
    val viewtype : Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = items[position]

        if(viewtype == 0)
            (holder as ViewHolder).content.text = item.content
        else
            (holder as ViewHolder2).content.text = item.content

        holder.itemView.setOnClickListener{
            if(viewtype == 1)
                itemClickListener.onClick(it, position)

        }
    }

    interface OnItemClickListener{
        fun onClick(v:View, position:Int)
    }

    fun setItemClickListener(OnItemClickListener : OnItemClickListener){
        this.itemClickListener = OnItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View?
        return when(viewtype){
            0 -> {
                val binding = CallenderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
            1 -> {
                val binding = CallenderItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder2(binding)
            }
            else -> throw RuntimeException("")
        }

    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(var binding : CallenderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val content = binding.content
    }
    class ViewHolder2(var binding : CallenderItem2Binding) : RecyclerView.ViewHolder(binding.root) {
        val content = binding.content
    }
}

