package com.example.week1

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week1.databinding.FragmentGalleryBinding
import org.jetbrains.anko.support.v4.toast
import java.sql.Date

import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvLayoutManager: GridLayoutManager
    private lateinit var rvAdapter: GalleryAdapter
    private lateinit var uri : String

    private val uriArr: ArrayList<String> = ArrayList()
    private val dateArr: ArrayList<Date> = ArrayList()

//    private fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
//        var ft: FragmentTransaction = fragmentManager.beginTransaction()
//        ft.detach(fragment).attach(fragment).commit()
//    }
    private fun loadImage() {


        val cursor = requireActivity().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")


        if(cursor!=null){
            while(cursor.moveToNext()){
                // 사진 경로 Uri 가져오기
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                uriArr.add(uri)
                val date = Date(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)))
                dateArr.add(date)
            }
            cursor.close()
        }
    }

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
//        Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_gallery, container, false)
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        binding.textview.text = param1
        binding.refreshGallery.setOnRefreshListener {
            toast("Refreshed!!")
            requireActivity().recreate()
//            refreshFragment(this, parentFragmentManager)

            // For library
//            binding.refreshGallery.setRefreshing(false)
            // For basic SwipeRefreshLayout
            binding.refreshGallery.isRefreshing = false
        }


        loadImage()
        // Change spanCount for number of columns
        val spanCount: Int = 3
        rvLayoutManager = GridLayoutManager(context, spanCount)
        rvAdapter = GalleryAdapter(this, uriArr, dateArr, spanCount)
        binding.recyclerView.apply {
            setLayoutManager(rvLayoutManager)
            setAdapter(rvAdapter)
        }

        rvAdapter.setItemClickListener(object : GalleryAdapter.OnItemClickListener{
            override fun onClick(v: View, position : Int){
                uri = uriArr[position]

                val dialog = ImageDialog(requireContext())
                dialog.showDialog(uri)
                dialog.setOnClickListener(object : ImageDialog.BtnClickListener{
                    override fun onClicked(change: String) {
                    }
                })
            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GalleryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}