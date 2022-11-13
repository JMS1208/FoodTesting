package com.capstone.foodtesting.data.model.naver.geo


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddressElement(
    @field:SerializedName("code")
    val code: String?,
    @field:SerializedName("longName")
    val longName: String?,
    @field:SerializedName("shortName")
    val shortName: String?,
    @field:SerializedName("types")
    val types: List<String?>?
)