package com.example.week1

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
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

        binding.phonelistview.addItemDecoration(DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL))

        binding.refreshContact.setOnRefreshListener {
            toast("Refreshed!!")
            requireActivity().recreate()
            binding.refreshContact.isRefreshing = false
        }
        // 내부저장소의 json파일에서 연락처+사진 정보 불러오기 ============================================ //

        val listfromjson = ArrayList<Phone>()
        val namelist = ArrayList<String>()

        val filename = "contacts.json"
        var jsonstr : String

        requireContext().openFileInput(filename).use { stream ->
            val text = stream.bufferedReader().use { it.readText() }
            jsonstr = text
            Log.d("TAG", "LOADED: $jsonstr")
        }
        val jsonary = JSONObject(jsonstr).getJSONArray("contacts")
        Log.d("TAG", "LOADED: $jsonary")


        for(index in 0 until jsonary.length()){
            val jsonobj = jsonary.getJSONObject(index)
            val img = jsonobj.getString("img")
            val name = jsonobj.getString("name")
            val number = jsonobj.getString("number")
            val phone = Phone(img, name, number)
            listfromjson.add(phone)
            namelist.add(name)
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

                if (!namelist.contains(name)) {
                    // json에 연락처 추가
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
                }
            }
            cursor.close()
        }
        listfromjson.sortWith(Comparator(OrderKoreanFirst::compare))

//        listfromjson.sortBy{it.name}

        val adapter = ContactAdapter(this, listfromjson)
        binding.phonelistview.adapter = adapter

        binding.fastScroller.setSectionIndexer(adapter)
        binding.fastScroller.attachRecyclerView(binding.phonelistview)

        adapter.setItemClickListener(object : ContactAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val tmpimg = listfromjson[position].img
                val tmpname = listfromjson[position].name
                val tmpnumber = listfromjson[position].number

                val dialog = ContactDialog(requireContext())
                dialog.showDialog(tmpimg, tmpname, tmpnumber)
                dialog.setOnClickListener(object : ContactDialog.BtnClickListener {
                    override fun onClicked(change: String) {
                        if(change == "yes") {
                            val dialog2 = GalleryDialog(requireContext())
                            dialog2.showDialog()
                            dialog2.setOnClickListener(object : GalleryDialog.ItemClickListener {
                                override fun onClicked(uri: String) {
                                    if (uri != "none" && uri != "cancel") {
                                        listfromjson[position].img = uri
                                        val jsonObjectlist = JSONArray()
                                        for (index in 0 until listfromjson.size) {
                                            val phoneobj = listfromjson[index]
                                            val newcontactjson = JSONObject()
                                            newcontactjson.put("img", phoneobj.img)
                                            newcontactjson.put("name", phoneobj.name)
                                            newcontactjson.put("number", phoneobj.number)
                                            jsonObjectlist.put(newcontactjson)
                                        }
                                        val jsonObject = JSONObject()
                                        jsonObject.put("contacts", jsonObjectlist)
                                        requireContext().openFileOutput(
                                            filename,
                                            Context.MODE_PRIVATE
                                        ).use {
                                            it.write(jsonObject.toString().toByteArray())
                                        }
                                        requireActivity().recreate()
                                    } else if (uri == "cancel") {
                                        dialog.showDialog(tmpimg, tmpname, tmpnumber)
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "사진을 선택해주세요!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            })
                        }
                        else{
                            val dialog2 = ImageDialog(requireContext())
                            dialog2.showDialog(tmpimg)
                            dialog2.setOnClickListener(object : ImageDialog.BtnClickListener{
                                override fun onClicked(change: String) {
                                    dialog.showDialog(tmpimg, tmpname, tmpnumber)
                                }
                            })
                        }
                    }
                })
            }
        })
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