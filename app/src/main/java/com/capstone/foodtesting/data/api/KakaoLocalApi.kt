package com.capstone.foodtesting.data.api

import android.os.Build
import com.capstone.foodtesting.BuildConfig
import com.capstone.foodtesting.data.model.kakao.local.KakaoLocalResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.Response
import retrofit2.http.Query

interface KakaoLocalApi {

    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_LOCAL_API_KEY}")
    @GET("/v2/local/geo/coord2address.json")
    suspend fun convertCoordToAddress(
        @Query("x") x: String,
        @Query("y") y: String
        ): Response<KakaoLocalResponse>
}