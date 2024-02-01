package com.android.imagesearchpage

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.android.imagesearchpage.databinding.FragmentMyboxBinding

// TODO: Rename parameter arguments, choose names that match

class MyboxFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var postList : ArrayList<Post>? = null
    private var _binding : FragmentMyboxBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postList = it.getParcelableArrayList("data",Post::class.java)
            Log.d("test", "잘들어왔는지 온크리트 확인 ${postList}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyboxBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", "잘들어왔는지 온크리트뷰 확인 ${postList}")
        val myBoxAdapter  = postList?.let { ImageAdapter(it.toMutableList(),2) }
        binding.rvMyList.adapter = myBoxAdapter
        binding.rvMyList.layoutManager = GridLayoutManager(context,2)
        binding.rvMyList.hasFixedSize()


        myBoxAdapter?.likeClick = object : ImageAdapter.LikeClick {
            override fun addItem(img: String, title: String, time: String, position: Int) {
                //
            }


            override fun deleteItem(img: String, title: String, time: String, position: Int) {
                postList?.removeIf {
                    it.img == img &&
                            it.title == title &&
                            it.time == time
                }
                myBoxAdapter?.deleteItems(position)
                Log.d("test", "deleteItem 실행됨 ${position}")
            }
        }




    }

    override fun onDestroy() {
        super.onDestroy()

        (activity as MainActivity).saveLikeList()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyboxFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(data: ArrayList<Post>) =
            MyboxFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("data",data)
                    Log.d("test", "잘들어왔는지  뉴인스턴스 확인 ${data}")
                }
            }
    }
}