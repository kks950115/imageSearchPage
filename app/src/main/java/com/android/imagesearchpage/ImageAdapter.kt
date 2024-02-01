package com.android.imagesearchpage


import android.app.Activity
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.imagesearchpage.databinding.ActivityMainBinding
import com.android.imagesearchpage.databinding.FragmentMyboxBinding
import com.android.imagesearchpage.databinding.RvItemBinding
import com.android.imagesearchpage.databinding.RvItemMyboxBinding
import com.bumptech.glide.Glide
import java.security.AccessController.getContext
import java.util.Locale

class ImageAdapter (val items: MutableList<Post>,viewType: Int) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var curItems = items
    private val curViewType = viewType
    interface LikeClick {
        fun addItem(img: String, title: String, time: String, position: Int)
        fun deleteItem(img: String, title: String, time: String, position: Int)
    }


    var likeClick : LikeClick? = null

    inner class SearchHolder(val binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root){
        val img = binding.ivImg
        val title =  binding.tvTitle
        val date = binding.tvDate
        val like = binding.ivLike
    }
    inner class MyBOxHolder(val binding: RvItemMyboxBinding) : RecyclerView.ViewHolder(binding.root){
        val img = binding.ivImg
        val title =  binding.tvTitle
        val date = binding.tvDate

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        if(viewType == Constants.SEARCH_HOLDER){
            val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return SearchHolder(binding)
        }else if(viewType == Constants.MYBOX_HOLDER){
            val binding = RvItemMyboxBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return MyBOxHolder(binding)
        }else {
            throw Exception("viewType을 찾지 못했습니다.")
        }
    }

    override fun getItemCount(): Int {
        return curItems.size
    }
    override fun getItemViewType(position: Int): Int {
        return curViewType
    }

    fun deleteItems(position : Int){
        curItems.removeAt(position)
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(curViewType){
            Constants.SEARCH_HOLDER -> {
                val holder = holder as SearchHolder

                holder.apply {
                    Glide.with(holder.itemView.context)
                        .load(curItems[position].img)
                        .placeholder(R.drawable.loading)
                        .into(holder.img)
                    title.text = curItems[position].title
                    date.text = formatTime(curItems[position].time)

                    like.isVisible=true

                    like.setOnClickListener {
                        if(like.tag=="heart"){
                            like.setImageResource(R.drawable.heart_empty)
                            like.tag = "heart_empty"
                            likeClick?.deleteItem(curItems[position].img,curItems[position].title,curItems[position].time,holder.adapterPosition)

                            Log.d("test","좋아요 리스트 클릭 삭제확인")
                        } else {
                            like.setImageResource(R.drawable.heart)
                            like.tag = "heart"
                            likeClick?.addItem(curItems[position].img,curItems[position].title,curItems[position].time,holder.adapterPosition)

                            Log.d("test","좋아요 리스트 클릭  추가확인")
                        }
                    }
                }
            }
            Constants.MYBOX_HOLDER -> {
                val holder = holder as MyBOxHolder

                holder.apply {
                    Glide.with(holder.itemView.context)
                        .load(curItems[position].img)
                        .placeholder(R.drawable.loading)
                        .into(holder.img)
                    title.text = curItems[position].title
                    date.text = formatTime(curItems[position].time)

                    holder.itemView.setOnClickListener {
                        likeClick?.deleteItem(curItems[position].img,curItems[position].title,curItems[position].time,holder.adapterPosition)
                        Log.d("test","마이박스 클릭 확인")
                    }
                }
            }
        }
    }

    private fun formatTime(str:String):String{

        val originalDate = str

        // SimpleDateFormat을 사용하여 문자열을 Date 객체로 파싱
        val DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val formattedoriginalDate = DateFormat.parse(originalDate)

        // SimpleDateFormat을 사용하여 Date 객체를 원하는 형식의 문자열로 포맷팅
        val targetDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDateString = targetDateFormat.format(formattedoriginalDate)
        return formattedDateString
    }
}