package com.example.week1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.week1.databinding.ItemViewBinding
import com.l4digital.fastscroll.FastScroller
import org.json.JSONArray
import org.json.JSONObject


class ContactAdapter(
    val context: ContactFragment,
    private val items: ArrayList<Phone>
    ) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(), FastScroller.SectionIndexer, Filterable {

    private var chs = arrayOf(
        "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
        "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
        "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
        "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    )

    private var contactSearchList: ArrayList<Phone> = ArrayList<Phone>()

    init {
        contactSearchList.addAll(items)
    }
    override fun getSectionText(position: Int): CharSequence {
        val ch = contactSearchList!![position].name[0]
        val code = ch.code

        // When Korean
        if (0xAC00 <= code && code <= 0xD7A3) {
            return chs[(((code - 0xAC00)/28)/21)]
        }
        // When English
        // Upper case
        if (65 <= code && code <= 90) {
            return ch.toString()
        }
        // Lower case
        if (97 <= code && code <= 122) {
//            return items[position].name[0].uppercaseChar().toString()
            return ch.uppercase()
        }
        // Number
//        Log.d("Check Section", ch.toString())
//        if (ch in '0'..'9') {
//
//            return "0~9"
//        }
        // Else Special Character
        return "#"
    }


    override fun getItemCount(): Int = contactSearchList!!.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                val filterResults = FilterResults()

                if (charString.isEmpty()) {
                    filterResults.values = items
                    filterResults.count = items.size
                } else {
                    val filteredList = ArrayList<Phone>()
                    //이부분에서 원하는 데이터를 검색할 수 있음
                    for (row in items) {
                        if (row.name.contains(charString) || row.number.contains(charString)) {
                            filteredList.add(row)
                        }
                    }
                    filterResults.values = filteredList
                    filterResults.count = filteredList.size
                }

                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                contactSearchList.clear()
                contactSearchList.addAll(filterResults.values as ArrayList<Phone>)
//                println(filterResults.values)
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {

        val item = contactSearchList!![position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked -> Name : ${item.name}, Number : ${item.number}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
//            itemView.tag = item
        }

        holder.itemView.setOnClickListener{

            // 클릭 리스너 간략화 구상
            (object : ContactAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    val tmpimg = contactSearchList[position].img
                    val tmpname = contactSearchList[position].name
                    val tmpnumber = contactSearchList[position].number

                    val dialog = ContactDialog(context.mainActivity)
                    dialog.showDialog(tmpimg, tmpname, tmpnumber)
                    dialog.setOnClickListener(object : ContactDialog.BtnClickListener {
                        override fun onClicked(change: String) {
                            if (change == "yes") {
                                val dialog2 = GalleryDialog(context.mainActivity)
                                dialog2.showDialog()
                                dialog2.setOnClickListener(object :
                                    GalleryDialog.ItemClickListener {
                                    override fun onClicked(uri: String) {
                                        if (uri != "none" && uri != "cancel") {
                                            contactSearchList[position].img = uri
                                            val jsonObjectlist = JSONArray()
                                            for (index in 0 until contactSearchList.size) {
                                                val phoneobj = contactSearchList[index]
                                                val newcontactjson = JSONObject()
                                                newcontactjson.put("img", phoneobj.img)
                                                newcontactjson.put("name", phoneobj.name)
                                                newcontactjson.put("number", phoneobj.number)
                                                jsonObjectlist.put(newcontactjson)
                                            }
                                            val jsonObject = JSONObject()
                                            jsonObject.put("contacts", jsonObjectlist)
                                            context.mainActivity.openFileOutput(
                                                "contacts.json",
                                                Context.MODE_PRIVATE
                                            ).use {
                                                it.write(jsonObject.toString().toByteArray())
                                            }
                                            context.mainActivity.recreate()
                                        } else if (uri == "cancel") {
                                            dialog.showDialog(tmpimg, tmpname, tmpnumber)
                                        } else {
                                            Toast.makeText(
                                                context.mainActivity,
                                                "사진을 선택해주세요!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                })
                            } else {
                                val dialog2 = ImageDialog(context.mainActivity)
                                dialog2.showDialog(tmpimg)
                                dialog2.setOnClickListener(object : ImageDialog.BtnClickListener {
                                    override fun onClicked(change: String) {
                                        dialog.showDialog(tmpimg, tmpname, tmpnumber)
                                    }
                                })
                            }
                        }
                    })
                }
            }).onClick(it, position)

//            itemClickListener.onClick(it, position)
        }
        val option1 = RequestOptions().circleCrop()

        with(holder) {
                Glide.with(context).load(item.img).centerCrop().apply(option1).into(binding.Image)
        }
    }

    interface OnItemClickListener{
        fun onClick(v:View, position:Int)
    }
//
//    fun setItemClickListener(OnItemClickListener : OnItemClickListener){
//        this.itemClickListener = OnItemClickListener
//    }
//
//    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // init SearchList
//        contactSearchList = items
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

