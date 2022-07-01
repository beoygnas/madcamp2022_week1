package com.example.week1

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.week1.databinding.ContactDialogBinding
import com.example.week1.databinding.FragmentContactBinding
import com.example.week1.databinding.FragmentMyBinding

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
        binding.textview.text = param1

        val list = ArrayList<Phone>()

        val listurl = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        val projections = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            , ContactsContract.CommonDataKinds.Phone.NUMBER)

        var cursor = requireActivity().contentResolver.query(listurl, projections, null, null, null)

        while(cursor?.moveToNext()?:false) {

            val id = cursor?.getString(0)
            var name = cursor?.getString(1).orEmpty()
            var number = cursor?.getString(2).orEmpty()
            val phone = Phone(ContextCompat.getDrawable(requireContext(), R.drawable.img)!!, name, number)
            list.add(phone)
        }

        val adapter = phoneAdapter(list)
        binding.phonelistview.adapter = adapter

        adapter.setItemClickListener(object : phoneAdapter.OnItemClickListener{
            override fun onClick(v:View, position : Int){
                var tmpimg = list[position].img
                var tmpname = list[position].name
                var tmpnumber = list[position].number

                val dialog = ContactDialog(requireContext())
                dialog.showDialog(tmpname, tmpnumber)
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