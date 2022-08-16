package com.capstone.foodtesting.data.api

import com.capstone.foodtesting.BuildConfig
import com.capstone.foodtesting.data.model.kakao.search.address.AddressSearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.Response

interface KakaoAddressSearchApi {

    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_LOCAL_API_KEY}")
    @GET("/v2/local/search/address.json")
    suspend fun searchAddress(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<AddressSearchResponse>
}