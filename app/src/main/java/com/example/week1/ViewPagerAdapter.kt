package com.example.week1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(strData: ArrayList<String>)
    : RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder>() {

    val arrData = strData

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtCount: TextView = itemView.findViewById(R.id.txtCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.viewpager_item, parent, false)
        val viewHolder = MyViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtCount.text = arrData[position]
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

}