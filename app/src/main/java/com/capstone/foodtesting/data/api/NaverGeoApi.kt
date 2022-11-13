package com.capstone.foodtesting.data.api

import com.capstone.foodtesting.data.model.naver.geo.NaverGeoResponse
import com.kakao.sdk.auth.Constants.CLIENT_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverGeoApi {

    @Headers("X-NCP-APIGW-API-KEY-ID: 35rvo5emf5"
        ,"X-NCP-APIGW-API-KEY: MpgmN1qDiZJ2LNYabp72IKl4HijnllLgF1UEF7jx")
    @GET("map-geocode/v2/geocode")
    suspend fun searchGeoInfo(
        @Query("query") query: String,
        @Query("coordinate") coordinate: String
    ): Response<NaverGeoResponse>
}