package com.example.week1

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week1.databinding.CallenderItemBinding
import com.example.week1.databinding.FragmentCallenderBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CallendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentCallenderBinding? = null
    private var _binding2: CallenderItemBinding? = null
    private val binding get() = _binding!!
    private val binding2 get() = _binding2!!

    lateinit var adapter : callendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCallenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var selected_year : Int = 0
        var selected_month : Int = 0
        var selected_day : Int = 0
        var datestr = ""
        var jsonString = ""
        var jsonObject = JSONObject()
        var jsonArray = JSONArray()
        var listfromjson = ArrayList<Schedule>()

//        adapter = callendarAdapter(this, listfromjson)
//        binding.calendarlist.adapter = adapter

        binding.calendarView.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            binding.textview.visibility = View.VISIBLE
            binding.btnframe.visibility = View.VISIBLE
            binding.btnremove.visibility = View.VISIBLE
            binding.btnadd.visibility = View.VISIBLE
            binding.btnok.visibility = View.INVISIBLE

            selected_year = year
            selected_month = month+1
            selected_day = dayOfMonth

            binding.textview.text = String.format("%d월 %d일", selected_month, selected_day)

            var str_year : String
            var str_month : String
            var str_day : String

            str_year = selected_year.toString()
            if(selected_month < 10) str_month = "0" + selected_month
            else str_month = selected_month.toString()
            if(selected_day < 10) str_day = "0" + selected_day
            else str_day = selected_day.toString()

            datestr = str_year + str_month + str_day

            var files : Array<String> = requireContext().fileList()
            if(!files.contains(datestr +".json")){
                requireContext().openFileOutput(datestr + ".json", Context.MODE_PRIVATE).use{
                    it.write("{\"${datestr}\" : []}".toByteArray())
                }
            }

            requireContext().openFileInput(datestr + ".json").use { stream ->
                val text = stream.bufferedReader().use { it.readText() }
                jsonString = text
            }

            jsonObject = JSONObject(jsonString)
            jsonArray = JSONArray()

            try {
                jsonArray = jsonObject.getJSONArray(datestr)
            }catch(e:JSONException){}

            listfromjson = ArrayList<Schedule>()

            var i = 0
            while(i<jsonArray.length()){
                val tmpobject = jsonArray.getJSONObject(jsonArray.length()-1 - i)
                val content = tmpobject.getString("content").orEmpty()
                val regdata = tmpobject.getString("regdata").orEmpty()
                val schedule = Schedule(content, regdata)
                listfromjson.add(schedule)
                i++
            }

            if(listfromjson.size == 0) {
                binding.emptytext.visibility = View.VISIBLE
                binding.calendarlist.visibility = View.INVISIBLE
            }
            else {
                adapter = callendarAdapter(this, listfromjson)
                binding.calendarlist.adapter = adapter

                binding.calendarlist.adapter?.notifyDataSetChanged()
                binding.emptytext.visibility = View.INVISIBLE
                binding.calendarlist.visibility = View.VISIBLE
            }
        }

        binding.btnadd.setOnClickListener{

            adapter = callendarAdapter(this, listfromjson)
            adapter.setviewtype(0)
            binding.calendarlist.adapter = adapter

            val dialog = CallendaraddDialog(requireContext())
            dialog.showDialog(selected_year, selected_month, selected_day)
            dialog.setOnClickListener(object : CallendaraddDialog.BtnClickListener{

                override fun onClicked(content: String, regdata : String) {
                    if(content != "") {
                        val newschedulejson = JSONObject()
                        newschedulejson.put("content", content)
                        newschedulejson.put("regdata", regdata)
                        jsonObject.accumulate(datestr, newschedulejson)
                        requireContext().openFileOutput(datestr + ".json", Context.MODE_PRIVATE).use {
                            it.write(jsonObject.toString().toByteArray())
                        }
                        listfromjson.add(0, Schedule(content, regdata))

                        binding.emptytext.visibility = View.INVISIBLE
                        binding.calendarlist.visibility = View.VISIBLE
                        adapter.notifyItemInserted(0);
                    }
                }

            })
        }

        binding.btnremove.setOnClickListener{

            adapter = callendarAdapter(this, listfromjson)
            adapter.setviewtype(1)
            binding.calendarlist.adapter = adapter

            //삭제
            adapter.setItemClickListener(object : callendarAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    val tmpcontent = listfromjson[position].content
                    val tmpregdata = listfromjson[position].regdata
                    val dialog = CallendarremoveDialog(requireContext())
                    var idx = 0
                    Log.d("content", tmpcontent)
                    Log.d("regdata", tmpregdata)

                    dialog.showDialog()
                    dialog.setOnClickListener(object : CallendarremoveDialog.BtnClickListener{
                        override fun onClicked(content: String) {

                            val newjsonArray = JSONArray()
                            for(index in 0 until listfromjson.size){
                                val obj = listfromjson[listfromjson.size - 1 - index]
                                if(tmpcontent == obj.content && tmpregdata == obj.regdata) {
                                    idx = (listfromjson.size-1)-index
                                    continue
                                }
                                val tmpobj = JSONObject()
                                tmpobj.put("content", obj.content)
                                tmpobj.put("regdata", obj.regdata)
                                newjsonArray.put(tmpobj)
                            }

                            Log.d("strprev" , listfromjson.toString())
                            listfromjson.removeAt(idx)
                            adapter.notifyDataSetChanged();
                            Log.d("strnxt" , listfromjson.toString())
                            Log.d("str" , ""+idx)

                            var newjsonObject = JSONObject()
                            newjsonObject.put(datestr, newjsonArray)
                            requireContext().openFileOutput(datestr+".json", Context.MODE_PRIVATE).use {
                                it.write(newjsonObject.toString().toByteArray())
                            }

                            if(listfromjson.size == 0) {
                                binding.emptytext.visibility = View.VISIBLE
                                binding.calendarlist.visibility = View.INVISIBLE
                            }
                        }
                    })
                }
            })
            binding.btnremove.visibility = View.INVISIBLE
            binding.btnadd.visibility = View.INVISIBLE
            binding.btnok.visibility = View.VISIBLE
        }

        binding.btnok.setOnClickListener{

            if(listfromjson.size == 0) {
                binding.emptytext.visibility = View.VISIBLE
                binding.calendarlist.visibility = View.INVISIBLE
            }
            else {
                adapter = callendarAdapter(this@CallendarFragment, listfromjson)
                adapter.viewtype = 0
                binding.calendarlist.adapter = adapter
                binding.emptytext.visibility = View.INVISIBLE
                binding.calendarlist.visibility = View.VISIBLE
            }

            binding.btnremove.visibility = View.VISIBLE
            binding.btnadd.visibility = View.VISIBLE
            binding.btnok.visibility = View.INVISIBLE
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CallendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}