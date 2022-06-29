package com.example.week1
import android.content.Context
import android.widget.BaseAdapter
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView

class phoneAdapter : BaseAdapter() {

    private var phoneList = ArrayList<Phone>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var view = convertView
        val context = parent!!.context

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.phone_item, parent, false)
        }

        val name = view!!.findViewById(R.id.name) as TextView
        val number = view.findViewById(R.id.number) as TextView

        val phoneitem = phoneList[position]

        // 아이템에 데이터 반영
        name.text = phoneitem.name
        number.text = phoneitem.number

        return view
    }

    // 아이템 데이터 추가를 위한 함수
    fun addItem(A: String, B: String) {
        val item = Phone()
        item.name = A
        item.number = B
        phoneList.add(item)
    }

    override fun getItem(position: Int): Any {
        return phoneList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return phoneList.size
    }
}