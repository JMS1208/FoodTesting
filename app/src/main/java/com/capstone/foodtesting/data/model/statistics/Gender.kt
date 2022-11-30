package com.capstone.foodtesting.data.model.statistics


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Gender(
    @field:SerializedName("0")
    val unknown: Int?,
    @field:SerializedName("1")
    val male: Int?,
    @field:SerializedName("2")
    val female: Int?
)