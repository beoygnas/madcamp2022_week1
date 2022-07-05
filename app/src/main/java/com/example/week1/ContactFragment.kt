package com.example.week1

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week1.databinding.FragmentContactBinding
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

object OrderKoreanFirst {
    private const val LEFT = -1
    private const val RIGHT = 1

    fun compare (left:Phone, right:Phone): Int {
        val _left = left.name.uppercase().filterNot(Char::isWhitespace)
        val _right = right.name.uppercase().filterNot(Char::isWhitespace)

        val (llen, rlen) = _left.length to _right.length
        val mlen = llen.coerceAtMost(rlen)

        for (i in 0 until mlen) {
            val (lc, rc) = _left[i] to _right[i]

            if(lc != rc) {
                return if(conditionKoreanAndEnglish(lc, rc) || conditionKoreanAndNumber(lc, rc) || conditionEnglishAndNumber(lc, rc) || conditionKoreanAndSpecial(lc, rc)) -(lc - rc)
                else if(conditionEnglishAndSpecial(lc, rc) || conditionNumberAndSpecial(lc, rc)) {
                    if(isEnglish(lc) || isNumber(lc)) LEFT else RIGHT
                } else { lc - rc }
            }
        }

        return llen - rlen
    }


    // 대문자로 치환 후 비교할 예정이므로 대문자 함수만
    private fun isEnglish(ch:Char) : Boolean = ch in 'A'..'Z'
    // 자음, 모음만 있는것도 한글로
    private fun isKorean(ch:Char) : Boolean = ch in 'ㄱ'..'ㅣ' || ch in '가'..'힣'
    private fun isNumber(ch:Char) : Boolean = ch in '0'..'9'
    private fun isSpecial(ch:Char) : Boolean = ch in '!'..'/' || ch in ':'..'@' || ch in '['..'`' || ch in '{'..'~'

    private fun conditionKoreanAndEnglish(c1: Char, c2: Char) = isKorean(c1) && isEnglish((c2)) || isEnglish(c1) && isKorean(c2)
    private fun conditionKoreanAndNumber(c1: Char, c2: Char) = isKorean(c1) && isNumber((c2)) || isNumber(c1) && isKorean(c2)
    private fun conditionKoreanAndSpecial(c1: Char, c2: Char) = isKorean(c1) && isSpecial((c2)) || isSpecial(c1) && isKorean(c2)
    private fun conditionEnglishAndNumber(c1: Char, c2: Char) = isEnglish(c1) && isNumber((c2)) || isNumber(c1) && isEnglish(c2)
    private fun conditionEnglishAndSpecial(c1: Char, c2: Char) = isEnglish(c1) && isSpecial((c2)) || isSpecial(c1) && isEnglish(c2)
    private fun conditionNumberAndSpecial(c1: Char, c2: Char) = isNumber(c1) && isSpecial((c2)) || isSpecial(c1) && isNumber(c2)

}

class ContactFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    lateinit var mainActivity: MainActivity

    var adapter: ContactAdapter? = null

    private val listfromjson = ArrayList<Phone>()
    private val checklist = ArrayList<Boolean>()
    private val filename = "contacts.json"



    var searchViewTextListener: SearchView.OnQueryTextListener =
        object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean = false // 검색버튼 입력시. 검색버튼 없으므로 사용 X
            override fun onQueryTextChange(s: String?): Boolean {
                adapter?.filter?.filter(s)
                binding.phonelistview.adapter = adapter
                binding.phonelistview.layoutManager = LinearLayoutManager(context)
                return false
            }
        }

    private fun loadContact() {

        // 내부저장소의 json파일에서 연락처+사진 정보 불러오기 ============================================ //

        // Refresh가 activity 전부를 초기화할땐 항상 Arr가 초기화되지만 notify로하면 초기화 필요할 것
        listfromjson.clear()
        checklist.clear()

        var jsonstr : String

        requireContext().openFileInput(filename).use { stream ->
            val text = stream.bufferedReader().use { it.readText() }
            jsonstr = text
        }

        val jsonary = JSONObject(jsonstr).getJSONArray("contacts")

        for(index in 0 until jsonary.length()){
            val jsonobj = jsonary.getJSONObject(index)
            val img = jsonobj.getString("img")
            val name = jsonobj.getString("name")
            val number = jsonobj.getString("number")
            val phone = Phone(img, name, number)
            listfromjson.add(phone)
            checklist.add(false)
        }

        // 내부저장소와 연락처 동기화  ========================================================= //
        // 내부저장소에 없는 연락처를 추가해줌.

        val listurl = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        val projections = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            , ContactsContract.CommonDataKinds.Phone.NUMBER)

        val cursor = requireActivity().contentResolver.query(listurl, projections, null, null, null)

        if(cursor!=null) {
            while (cursor.moveToNext()) {

                val name = cursor.getString(0).orEmpty()
                val number = cursor.getString(1).orEmpty()
                var idx = -1

                for(index in 0 until listfromjson.size){
                    val obj = listfromjson[index]
                    if(obj.name == name && obj.number == number) {
                        checklist[index] = true
                        idx = index
                    }
                }
                if(idx == -1){
                    val jsonObject = JSONObject(jsonstr)
                    val newcontactjson = JSONObject()
                    val imageUri: String = "android.resource://com.example.week1/" + R.drawable.icon_contact_basic

                    newcontactjson.put("img", imageUri)
                    newcontactjson.put("name", name)
                    newcontactjson.put("number", number)
                    jsonObject.accumulate("contacts", newcontactjson)
                    requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
                        it.write(jsonObject.toString().toByteArray())
                    }
                    // jsonlist에 추가
                    val phone = Phone(imageUri, name, number)
                    listfromjson.add(phone)
                    checklist.add(true)
                }
                Log.d("str", listfromjson.toString())
            }
            cursor.close()


            var newlistfromjson = ArrayList<Phone>()
            val newjsonArray = JSONArray()

            for(index in 0 until listfromjson.size){
                if(checklist[index] == false)
                    continue
                else{
                    newlistfromjson.add(listfromjson[index])
                    val obj = listfromjson[index]
                    val tmpobj = JSONObject()
                    tmpobj.put("img", obj.img)
                    tmpobj.put("name", obj.name)
                    tmpobj.put("number", obj.number)
                    newjsonArray.put(tmpobj)
                }
            }
            listfromjson.clear()
            listfromjson.addAll(newlistfromjson)

            var newjsonObject = JSONObject()
            newjsonObject.put("contacts", newjsonArray)
            requireContext().openFileOutput("contacts.json", Context.MODE_PRIVATE).use {
                it.write(newjsonObject.toString().toByteArray())
            }
        }
        listfromjson.sortWith(Comparator(OrderKoreanFirst::compare))
        println(listfromjson)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val files : Array<String> = requireContext().fileList()
        if(!files.contains("contacts.json")){
            requireContext().openFileOutput("contacts.json", Context.MODE_PRIVATE).use{
            it.write("{\"contacts\" : []}".toByteArray())
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_contact, container, false)
        _binding = FragmentContactBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onDestroyView() {
        binding.fastScroller.detachRecyclerView()
        _binding = null
        super.onDestroyView()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.searchView.setOnQueryTextListener(searchViewTextListener)

        binding.phonelistview.addItemDecoration(DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL))

        binding.refreshContact.setOnRefreshListener {
            toast("Refreshed!!")
            // Load Contact from Phone
            // Update adapter
            // Notify
            loadContact()
            adapter = ContactAdapter(this, listfromjson)

            binding.phonelistview.adapter = adapter

//            requireActivity().recreate()
            binding.refreshContact.isRefreshing = false
        }


        loadContact()
//        listfromjson.sortBy{it.name}

        adapter = ContactAdapter(this, listfromjson)
        binding.phonelistview.adapter = adapter

        binding.fastScroller.setSectionIndexer(adapter)
        binding.fastScroller.attachRecyclerView(binding.phonelistview)

    }
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}