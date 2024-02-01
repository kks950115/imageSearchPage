package com.android.imagesearchpage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.android.identity.android.legacy.Utility
import com.android.imagesearchpage.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {
    private val likeList = arrayListOf<Post>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //loadLikeList()
        binding.bottomNavigationView.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.search -> {
                        val home = SearchFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.frag_main, home)
                            .commit()
                    }

                    R.id.myBox -> {
                        Log.d("test","메인액티비티 리스트 ${likeList} ")
                        val myBox = MyboxFragment.newInstance(likeList)
                        supportFragmentManager.beginTransaction().replace(R.id.frag_main, myBox)
                            .commit()
                    }

                    else -> {
                        val home = SearchFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.frag_main, home)
                            .commit()
                    }
                }
                true
            }
            selectedItemId = R.id.search
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        return super.dispatchTouchEvent(ev)
    }
    fun getLikeList() : ArrayList<Post> {
        return likeList
    }

    fun saveLikeList(){
        val pref = getSharedPreferences("sp",0)
        val edit = pref.edit()
        val gson = GsonBuilder().create()
        val target = gson.toJson(likeList)
        edit.putString("likeList",target)
        edit.apply()
    }
    private fun loadLikeList(){
        val pref = getSharedPreferences("sp",0)
        val value = pref.getString("likeList","")
        val gson = GsonBuilder().create()
        val ll = gson.fromJson(value,Post::class.java)
        likeList.add(ll)
    }


//    override fun deleteItem2(img: String, title: String, time: String) {
//        likeList.add(
//            Post(img, title, time)
//        )
//        Log.d("test","메인 액티 deleteitem :$img , $title , $time")
//    }

//    private fun setFragment(frag : Fragment) {
//        supportFragmentManager.commit {
//            replace(R.id.frameLayout, frag)
//            setReorderingAllowed(true)
//            addToBackStack("")
//        }
//    }

}