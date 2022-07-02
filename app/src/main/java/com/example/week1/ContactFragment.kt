package com.example.week1

import android.content.Context
import android.icu.lang.UCharacter
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.week1.databinding.ContactDialogBinding
import com.example.week1.databinding.FragmentContactBinding
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentContactBinding? = null
    private var _dialogbinding : ContactDialogBinding?= null
    private val binding get() = _binding!!
    private val dialogbinding get() = _dialogbinding!!

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


        var files : Array<String> = requireContext().fileList()
        if(!files.contains("contacts.json")){
            requireContext().openFileOutput("contacts.json", Context.MODE_PRIVATE).use{
            it.write("{\"contacts\" : []}".toByteArray())
            }
        }
//        val filename = "contacts.json"
//        var jsonstr : String

//  assets json 읽기
//        val assetManager = resources.assets
//        val inputStream= assetManager.open("contacts.json")
//        val jsonString = inputStream.bufferedReader().use { it.readText() }
//        requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use{
//            it.write(jsonString.toByteArray())
//        }
//  파일 읽기
//        requireContext().openFileInput(filename).use { stream ->
//            val text = stream.bufferedReader().use { it.readText() }
//            jsonstr = text
//            Log.d("TAG", "LOADED: $jsonstr")
//        }
//  새로운 연락처 추가
//        val jsonObject = JSONObject(jsonstr)
//        val newcontactjson = JSONObject()
//        newcontactjson.put("img", 2131165296)
//        newcontactjson.put("name", "kimsangyeob")
//        newcontactjson.put("number", "01026474429")
//        jsonObject.accumulate("contacts", newcontactjson)

//  파일 쓰기
//        requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use{
//            it.write(jsonObject.toString().toByteArray())
//            Log.d("TAG", "응애: $jsonObject.toString()")
//        }

//            얘네는 인자확인용 코드
//            val contactsjsonObject = jsonArray.getJSONObject(1)
//            val value = contactsjsonObject.getString("name")
//            Log.d("TAG", value)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_contact, container, false)
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        _dialogbinding = ContactDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        _dialogbinding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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
            val img = jsonobj.getInt("img")
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

            var cursor = requireActivity().contentResolver.query(listurl, projections, null, null, null)

            while(cursor?.moveToNext()?:false) {

                var name = cursor?.getString(0).orEmpty()
                var number = cursor?.getString(1).orEmpty()

                if(!namelist.contains(name)){
                    // json에 연락처 추가
                    val jsonObject = JSONObject(jsonstr)
                    val newcontactjson = JSONObject()
                    newcontactjson.put("img", R.drawable.img)
                    newcontactjson.put("name", name)
                    newcontactjson.put("number", number)
                    jsonObject.accumulate("contacts", newcontactjson)
                    requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use{
                    it.write(jsonObject.toString().toByteArray()) }
                    // jsonlist에 추가
                    val phone = Phone(R.drawable.img, name, number)
                    listfromjson.add(phone)
                }
            }

        val adapter = phoneAdapter(listfromjson)
        binding.phonelistview.adapter = adapter

        adapter.setItemClickListener(object : phoneAdapter.OnItemClickListener{
            override fun onClick(v:View, position : Int){
                var tmpimg = listfromjson[position].img
                var tmpname = listfromjson[position].name
                var tmpnumber = listfromjson[position].number

                val dialog = ContactDialog(requireContext())
                dialog.showDialog(tmpimg, tmpname, tmpnumber)
            }
        })


        binding.phonelistview.addItemDecoration(
            DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL)
        )
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