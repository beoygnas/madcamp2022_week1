package com.example.week1

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.week1.databinding.FragmentGalleryBinding
import com.example.week1.databinding.FragmentProfilegalleryBinding
import org.jetbrains.anko.support.v4.toast


class ProfilegalleryFragment : Fragment() {

    private var _binding: FragmentProfilegalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var profilegalleryAdapter: ProfilegalleryAdapter
//    private lateinit var imageList : List<Image>

    private val uriArr: ArrayList<String> = ArrayList<String>()

    private fun loadImage() {
//        Log.d("Check", "LoadImage called")
//        uriArr = ArrayList<String>()
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
//                Log.d("Uri", "Uri is travelsed")
            }
            cursor.close()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilegalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.refreshProfile.setOnRefreshListener {
            toast("Refreshed!!")
            requireActivity().recreate()
            binding.refreshProfile.isRefreshing = false
        }

        loadImage()
        binding.recyclerViewProfile.layoutManager = GridLayoutManager(context, 3)
//        Log.d("Uri: ", uriArr) : Doesn't work when there is no photo, it works fine with photos
        profilegalleryAdapter = ProfilegalleryAdapter(this, uriArr)
        binding.recyclerViewProfile.adapter = profilegalleryAdapter
    }

}