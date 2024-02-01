package com.android.imagesearchpage

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


interface KakaoImageAPI {
    //@Headers("Authorization: KakaoAK ${Constants.KAKAO_AUTHKEY}")
    @GET("v2/search/image")
    suspend fun getPost(
        @Header("Authorization") apiKey : String,
        @Query("query") searchKeyword: String
    ) : APIResponse
}