package com.android.imagesearchpage

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    @SerializedName("thumbnail_url")
    val img : String,
    @SerializedName("display_sitename")
    val title : String,
    @SerializedName("datetime")
    val time : String
) :Parcelable

data class MetaData(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount : Int,
    @SerializedName("is_end")
    val isEnd:Boolean
)

data class APIResponse(
    @SerializedName("meta")
    val metaData : MetaData,
    val documents : MutableList<Post>
)

data class KakaoImg (val imgResponse: APIResponse)