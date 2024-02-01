package com.android.imagesearchpage


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.android.imagesearchpage.databinding.FragmentSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var posts = mutableListOf<Post>()
    private lateinit var likeList: ArrayList<Post>


    fun getLikeList():ArrayList<Post>{
        return likeList
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        likeList = (activity as MainActivity).getLikeList()
        Log.d("test", "검색프래그먼트 확인 ${likeList}")
        loadWord()

        binding.btnSearch.setOnClickListener {
            val keyword = binding.etSearch.text.toString()
            loadImage(keyword)
            saveWord()
        }

        //var imgAdapter = ImageAdapter(posts)



    }

    private fun loadImage(str:String)  {
        CoroutineScope(Dispatchers.Main).launch {
            val responseData = getPosts(str)
            posts = responseData.documents
            val imgAdapter = ImageAdapter(posts,1)

            imgAdapter.likeClick = object : ImageAdapter.LikeClick {
                override fun addItem(img: String, title: String, time: String, position: Int) {
                    val post = Post(img,title,time)
                    if(!likeList.contains(post)) likeList.add(post)

                    //Log.d("test", "addItem 실행됨 ${likeList}")
                }

                override fun deleteItem(img: String, title: String, time: String, position: Int) {
                    likeList.removeIf {
                        it.img == img &&
                        it.title == title &&
                        it.time == time
                    }

                    //Log.d("test", "deleteItem 실행됨 ${likeList}")
                }
            }

            binding.rvImgItem.adapter =  imgAdapter
            binding.rvImgItem.layoutManager = GridLayoutManager(context,2)
            binding.rvImgItem.hasFixedSize()
        }
    }

    private suspend fun getPosts(str:String) = withContext(Dispatchers.IO){
        NetWorkClient.imageNetwork.getPost(Constants.KAKAO_AUTHKEY,str)
    }

    private fun saveWord(){
        val pref = requireActivity().getSharedPreferences("word",0)
        val edit = pref.edit()
        edit.putString("word",binding.etSearch.text.toString())
        edit.apply()
    }
    private fun loadWord(){
        val pref = requireActivity().getSharedPreferences("word",0)
        binding.etSearch.setText(pref.getString("word",""))
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}