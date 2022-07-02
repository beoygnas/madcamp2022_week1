package com.example.week1

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.AssetManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.week1.databinding.ContactDialogBinding
import com.example.week1.databinding.FragmentContactBinding
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.nio.channels.AsynchronousFileChannel.open

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

        var profilechange : String = "no"

        adapter.setItemClickListener(object : phoneAdapter.OnItemClickListener{
            override fun onClick(v:View, position : Int){
                var tmpimg = listfromjson[position].img
                var tmpname = listfromjson[position].name
                var tmpnumber = listfromjson[position].number

                val dialog = ContactDialog(requireContext())
                dialog.showDialog(tmpimg, tmpname, tmpnumber)
                dialog.setOnClickListener(object : ContactDialog.BtnClickListener{
                    override fun onClicked(change: String) {
                        profilechange = change
                        val transaction = requireActivity().supportFragmentManager.beginTransaction().replace(R.id.tmp, ProfilegalleryFragment())
                        transaction.addToBackStack(null)
                        transaction.commit()
                        Toast.makeText(context, profilechange, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })

//        if(profilechange == "yes"){
//            Log.d("String" , "yesyesyes")
//
//        }
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