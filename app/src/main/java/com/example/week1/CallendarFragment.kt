package com.example.week1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.week1.databinding.FragmentCallenderBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CallendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentCallenderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        var files : Array<String> = requireContext().fileList()
        if(!files.contains("schedules.json")){
            requireContext().openFileOutput("schedules.json", Context.MODE_PRIVATE).use{
                it.write("{\"schedules\" : }".toByteArray())
            }
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

    fun addjson(content : String, regdata : String){

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val assetManager = resources.assets
        val inputStream= assetManager.open("schedules.json")
        var jsonString = inputStream.bufferedReader().use { it.readText() }


        var selected_year : Int = 0
        var selected_month : Int = 0
        var selected_day : Int = 0


        binding.calendarView.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            binding.textview.visibility = View.VISIBLE
            binding.refreshschedule.visibility = View.VISIBLE
            binding.btnframe.visibility = View.VISIBLE
            selected_year = year
            selected_month = month+1
            selected_day = dayOfMonth
            binding.textview.text = String.format("%d월 %d일 일정", selected_month, selected_day)

            var str_year : String
            var str_month : String
            var str_day : String

            str_year = selected_year.toString()
            if(selected_month < 10) str_month = "0" + selected_month
            else str_month = selected_month.toString()
            if(selected_day < 10) str_day = "0" + selected_day
            else str_day = selected_day.toString()

            var datestr = str_year + str_month + str_day
            Log.d("str", datestr)

            var jsonObject = JSONObject(jsonString)
            var jsonArray : JSONArray = JSONArray()
            try {
                jsonArray = jsonObject.getJSONArray(datestr)
            }catch(e:JSONException){
            }

            var listfromjson = ArrayList<Schedule>()

            var i = 0
            while(i<jsonArray.length()){
                val tmpobject = jsonArray.getJSONObject(i)
                val content = tmpobject.getString("content").orEmpty()
                val regdata = tmpobject.getString("regdata").orEmpty()
                val schedule = Schedule(content, regdata)
                listfromjson.add(schedule)
                i++
            }

            listfromjson.sortBy{it.regdata}
            val adapter = callendarAdapter(this, listfromjson)
            binding.calendarlist.adapter = adapter
        }

        binding.btnadd.setOnClickListener{
            val dialog = CallendaraddDialog(requireContext())
            dialog.showDialog(selected_year, selected_month, selected_day)
            dialog.setOnClickListener(object : CallendaraddDialog.BtnClickListener{
                override fun onClicked(content: String, regdata : String) {
                    if(content != "") {
                        Log.d("str", content)
                        addjson(content, regdata)
                    }
                    else{
                        Log.d("str", "비어있음")
                    }
                    requireActivity().recreate()
                }
            })

        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyFragment.
         */
        // TODO: Rename and change types and number of parameters
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