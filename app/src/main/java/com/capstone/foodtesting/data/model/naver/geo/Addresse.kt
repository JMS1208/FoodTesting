package com.capstone.foodtesting.data.model.naver.geo


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Addresse(
    @field:SerializedName("addressElements")
    val addressElements: List<AddressElement?>?,
    @field:SerializedName("distance")
    val distance: Double?,
    @field:SerializedName("englishAddress")
    val englishAddress: String?,
    @field:SerializedName("jibunAddress")
    val jibunAddress: String?,
    @field:SerializedName("roadAddress")
    val roadAddress: String?,
    @field:SerializedName("x")
    val x: String?,
    @field:SerializedName("y")
    val y: String?
)