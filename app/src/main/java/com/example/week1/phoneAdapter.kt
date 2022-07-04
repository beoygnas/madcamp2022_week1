package com.example.week1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.week1.databinding.ItemViewBinding
import com.l4digital.fastscroll.FastScroller


class phoneAdapter(
    val context: ContactFragment,
    private val items: ArrayList<Phone>
    ) : RecyclerView.Adapter<phoneAdapter.ViewHolder>(), FastScroller.SectionIndexer {

    var chs = arrayOf(
        "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
        "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
        "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
        "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    )

    override fun getSectionText(position: Int): CharSequence? {
        val code = items[position].name[0].code

        // When Korean
        if (0xAC00 <= code && code <= 0xD7A3) {
            return chs[(((code - 0xAC00)/28)/21)].toString()
        }
        // When English


        return items[position].name[0].toString()
    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: phoneAdapter.ViewHolder, position: Int) {

        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked -> Name : ${item.name}, Number : ${item.number}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
//            itemView.tag = item
        }
        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
        val option1 = RequestOptions().circleCrop()

        with(holder) {
                Glide.with(context).load(item.img).centerCrop().apply(option1).into(binding.Image)
        }
    }

    interface OnItemClickListener{
        fun onClick(v:View, position:Int)
    }

    fun setItemClickListener(OnItemClickListener : OnItemClickListener){
        this.itemClickListener = OnItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(var binding : ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Phone) {
//            binding.Image.setImageURI(item.img.toUri())

            binding.Name.text = item.name
            binding.Number.text = item.number
        }
    }
}

