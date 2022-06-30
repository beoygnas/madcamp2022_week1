package com.example.week1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.week1.databinding.FragmentContactBinding
import com.example.week1.databinding.ItemViewBinding


class phoneAdapter(private val items: ArrayList<Phone>) : RecyclerView.Adapter<phoneAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: phoneAdapter.ViewHolder, position: Int) {

        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked -> Name : ${item.name}, Number : ${item.number}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
//        return phoneAdapter.ViewHolder(inflatedView)
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(private var binding : ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Phone) {
            binding.Name.text = item.name
            binding.Number.text = item.number
        }
    }
}

