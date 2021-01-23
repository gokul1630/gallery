package com.gokul.gallery.data.api

import com.gokul.gallery.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnSplashApi {

companion object{
    const val URL="https://api.unsplash.com"
    private const val API_KEY=BuildConfig.API_KEY
}

    @Headers("Accept-Version: v1", "Authorization: Client-ID $API_KEY")
    @GET("search/photos")
    suspend fun getPhotos(
        @Query("query") query: String,
        @Query("page") page:Int,
        @Query("per_page") per_page:Int
    ): UnSplashResponse
}