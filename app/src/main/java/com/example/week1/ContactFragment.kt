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
import com.example.week1.databinding.ContactDialogBinding
import com.example.week1.databinding.FragmentContactBinding
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

        var cursor = requireActivity().contentResolver.query(listurl, projections, null, null, null)

        while(cursor?.moveToNext()?:false) {

            var name = cursor?.getString(0).orEmpty()
            var number = cursor?.getString(1).orEmpty()

            if (!namelist.contains(name)) {
                // json에 연락처 추가
                val jsonObject = JSONObject(jsonstr)
                val newcontactjson = JSONObject()

                var imageUri: String = "android.resource://com.example.week1/" + R.drawable.img

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

            listfromjson.sortBy{it.name}

            val adapter = phoneAdapter(this, listfromjson)
            binding.phonelistview.adapter = adapter

            adapter.setItemClickListener(object : phoneAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    var tmpimg = listfromjson[position].img
                    var tmpname = listfromjson[position].name
                    var tmpnumber = listfromjson[position].number

                    val dialog = ContactDialog(requireContext())
                    dialog.showDialog(tmpimg, tmpname, tmpnumber)
                    dialog.setOnClickListener(object : ContactDialog.BtnClickListener {
                        override fun onClicked(change: String) {
                            if(change == "yes") {
                                val dialog2 = GalleryDialog(requireContext())
                                dialog2.showDialog()
                                dialog2.setOnClickListener(object : GalleryDialog.itemClickListener {
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