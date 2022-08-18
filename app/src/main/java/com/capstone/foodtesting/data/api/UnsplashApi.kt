package com.capstone.foodtesting.data.api



import com.capstone.foodtesting.BuildConfig
import com.capstone.foodtesting.data.model.unsplash.UnsplashResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {

    @Headers("Authorization: Client-ID ${BuildConfig.UNSPLASH_API_KEY}")
    @GET("/search/photos")
    suspend fun getKoreanFoods(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("order_by") order_by: String
    ): Response<UnsplashResponse>


}