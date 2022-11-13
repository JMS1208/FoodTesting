package com.capstone.foodtesting.data.model.naver.geo


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NaverGeoResponse(
    @field:SerializedName("addresses")
    val addresses: List<Addresse?>?,
    @field:SerializedName("errorMessage")
    val errorMessage: String?,
    @field:SerializedName("meta")
    val meta: Meta?,
    @field:SerializedName("status")
    val status: String?
)